/*
 * This file ("MethodHandler.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.misc;

import de.canitzp.rarmor.api.internal.IMethodHandler;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.config.Config;
import de.canitzp.rarmor.data.WorldData;
import de.canitzp.rarmor.inventory.ContainerRarmor;
import de.canitzp.rarmor.module.color.ActiveModuleColor;
import de.canitzp.rarmor.module.main.ActiveModuleMain;
import de.canitzp.rarmor.packet.PacketHandler;
import de.canitzp.rarmor.data.RarmorData;
import de.canitzp.rarmor.item.ItemRarmor;
import de.canitzp.rarmor.packet.PacketOpenModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.UUID;

public class MethodHandler implements IMethodHandler {

    @Override
    public ItemStack getHasRarmorInSlot(Entity entity, EquipmentSlotType slot){
        if(entity instanceof LivingEntity){
            ItemStack stack = ((LivingEntity)entity).getItemStackFromSlot(slot);
            if(!stack.isEmpty()){
                Item item = stack.getItem();
                if(item instanceof ItemRarmor && ((ItemRarmor)item).getEquipmentSlot() == slot){
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public IRarmorData getDataForChestplate(PlayerEntity player, boolean createIfAbsent){
        ItemStack stack = this.getHasRarmorInSlot(player, EquipmentSlotType.CHEST);
        if(!stack.isEmpty()){
            return this.getDataForStack(player.getEntityWorld(), stack, createIfAbsent);
        }
        return null;
    }

    @Override
    public UUID checkAndSetRarmorId(ItemStack stack, boolean createIfAbsent){
        if(stack.getItem() instanceof ItemRarmor){
            if(!stack.hasTag()){
                if(createIfAbsent){
                    stack.setTag(new CompoundNBT());
                } else {
                    return null;
                }
            }
    
            CompoundNBT compound = stack.getTag();
            if(!compound.hasUniqueId("RarmorId")){
                if(createIfAbsent){
                    UUID id = UUID.randomUUID();
                    compound.putUniqueId("RarmorId", id);
                    return id;
                } else {
                    return null;
                }
            } else {
                return compound.getUniqueId("RarmorId");
            }
        } else {
            return null;
        }
    }

    @Override
    public void openRarmor(PlayerEntity player, int moduleId, boolean alsoSetData, boolean sendRarmorDataToClient){
        IRarmorData data = this.getDataForChestplate(player, true);
        if(data != null){
            if(alsoSetData){
                data.selectModule(moduleId);
            }

            boolean shouldOpenGui;
            if(sendRarmorDataToClient){
                boolean doPacket = Config.GENERAL.OPEN_CONFIRMATION_PACKET.get();

                shouldOpenGui = !doPacket;
                data.queueUpdate(true, doPacket ? moduleId : -1, true);
            }
            else{
                shouldOpenGui = true;
            }

            if(shouldOpenGui){
                player.openContainer(new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName(){
                        return new StringTextComponent("Rarmor");
                    }
    
                    @Override
                    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player){
                        return new ContainerRarmor(windowId, player, data.getCurrentModules().get(data.getCurrentModules().size() <= data.getSelectedModule() ? 0 : data.getSelectedModule()));
                    }
                });
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openRarmorFromClient(int moduleId, boolean alsoSetData, boolean sendRarmorDataToClient){
        PacketHandler.channel.sendToServer(new PacketOpenModule(moduleId, alsoSetData, sendRarmorDataToClient));
    }

    @Override
    public boolean mergeItemStack(Container container, ItemStack stack, int startIndexIncl, int endIndexExcl, boolean reverseDirection){
        return container instanceof ContainerRarmor && ((ContainerRarmor)container).mergeItemStack(stack, startIndexIncl, endIndexExcl, reverseDirection);
    }

    @Override
    public IRarmorData getDataForStack(World world, ItemStack stack, boolean createIfAbsent){
        UUID stackId = this.checkAndSetRarmorId(stack, !world.isRemote && createIfAbsent);
        if(stackId != null){
            if(world instanceof ServerWorld){
                Map<UUID, IRarmorData> allData = WorldData.getRarmorData((ServerWorld) world);
                IRarmorData data = allData.get(stackId);
                if(data == null){
                    if(createIfAbsent){
                        data = this.createRarmorData(stack);
                        allData.put(stackId, createRarmorData(stack));
                        return data;
                    }
                } else {
                    ItemStack bound = data.getBoundStack();
                    if(bound != null || bound != stack){
                        data.setBoundStack(stack);
                    }
                    return data;
                }
            } else if(createIfAbsent) {
                return this.createRarmorData(stack);
            }
        }
        return null;
    }
    
    private RarmorData createRarmorData(ItemStack stack){
        RarmorData data = new RarmorData(stack);
        //main module
        ActiveRarmorModule mainModule = Helper.initiateModuleById(ActiveModuleMain.IDENTIFIER, data);
        mainModule.onInstalled(null);
        data.getCurrentModules().add(mainModule);
        //color module
        ActiveRarmorModule colorModule = Helper.initiateModuleById(ActiveModuleColor.IDENTIFIER, data);
        colorModule.onInstalled(null);
        data.getCurrentModules().add(colorModule);
    
        data.setDirty(false);
    
        //data.queueUpdate();
        return data;
    }

    @Override
    public boolean compareModules(ActiveRarmorModule module, Object o){
        return o instanceof ActiveRarmorModule && ((ActiveRarmorModule)o).getIdentifier().equals(module.getIdentifier());
    }
}
