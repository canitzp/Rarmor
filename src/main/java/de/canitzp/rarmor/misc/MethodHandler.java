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
import de.canitzp.rarmor.data.RarmorDataCapability;
import de.canitzp.rarmor.data.WorldData;
import de.canitzp.rarmor.inventory.ContainerRarmor;
import de.canitzp.rarmor.module.color.ActiveModuleColor;
import de.canitzp.rarmor.module.main.ActiveModuleMain;
import de.canitzp.rarmor.packet.PacketHandler;
import de.canitzp.rarmor.data.RarmorData;
import de.canitzp.rarmor.item.ItemRarmor;
import de.canitzp.rarmor.packet.PacketOpenModule;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Map;
import java.util.UUID;

public class MethodHandler implements IMethodHandler {

    @Override
    public ItemStack getHasRarmorInSlot(Entity entity, EquipmentSlot slot){
        if(entity instanceof LivingEntity){
            ItemStack stack = ((LivingEntity)entity).getItemBySlot(slot);
            if(!stack.isEmpty()){
                Item item = stack.getItem();
                if(item instanceof ItemRarmor && LivingEntity.getEquipmentSlotForItem(stack) == slot){
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public IRarmorData getDataForChestplate(Player player, boolean createIfAbsent){
        ItemStack stack = this.getHasRarmorInSlot(player, EquipmentSlot.CHEST);
        if(!stack.isEmpty()){
            return this.getDataForStack(player.getLevel(), stack, createIfAbsent);
        }
        return null;
    }

    @Override
    public UUID checkAndSetRarmorId(ItemStack stack, boolean createIfAbsent){
        if(stack.getItem() instanceof ItemRarmor){
            if(!stack.hasTag()){
                if(createIfAbsent){
                    stack.setTag(new CompoundTag());
                } else {
                    return null;
                }
            }

            CompoundTag compound = stack.getTag();
            if(!compound.hasUUID("RarmorId")){
                if(createIfAbsent){
                    UUID id = UUID.randomUUID();
                    compound.putUUID("RarmorId", id);
                    return id;
                } else {
                    return null;
                }
            } else {
                return compound.getUUID("RarmorId");
            }
        } else {
            return null;
        }
    }

    @Override
    public void openRarmor(Player player, int moduleId, boolean alsoSetData, boolean sendRarmorDataToClient){
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
                player.openMenu(new MenuProvider() {
                    @Override
                    public TextComponent getDisplayName(){
                        return new TextComponent("Rarmor");
                    }
    
                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player){
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
    public boolean mergeItemStack(AbstractContainerMenu container, ItemStack stack, int startIndexIncl, int endIndexExcl, boolean reverseDirection){
        return container instanceof ContainerRarmor && ((ContainerRarmor)container).moveItemStackTo(stack, startIndexIncl, endIndexExcl, reverseDirection);
    }

    @Override
    public IRarmorData getDataForStack(Level world, ItemStack stack, boolean createIfAbsent) {
        UUID stackId = this.checkAndSetRarmorId(stack, !world.isClientSide() && createIfAbsent);
        if (stackId != null) {
            Map<UUID, IRarmorData> allData = WorldData.getRarmorData();
            IRarmorData data = allData.get(stackId);
            if(data != null){
                ItemStack bound = data.getBoundStack();
                if (!bound.isEmpty() || bound != stack) {
                    data.setBoundStack(stack);
                }
                return data;
            } else if(createIfAbsent){
                data = this.createRarmorData(stack);
                allData.put(stackId, createRarmorData(stack));
                return data;
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
        //ActiveRarmorModule colorModule = Helper.initiateModuleById(ActiveModuleColor.IDENTIFIER, data);
        //colorModule.onInstalled(null);
        //data.getCurrentModules().add(colorModule);
    
        data.setDirty(false);
    
        //data.queueUpdate();
        return data;
    }

    @Override
    public boolean compareModules(ActiveRarmorModule module, Object o){
        return o instanceof ActiveRarmorModule && ((ActiveRarmorModule)o).getIdentifier().equals(module.getIdentifier());
    }
}
