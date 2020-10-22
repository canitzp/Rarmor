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
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.data.RarmorData;
import de.canitzp.rarmor.item.ItemRarmor;
import de.canitzp.rarmor.packet.PacketOpenModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.UUID;

public class MethodHandler implements IMethodHandler {

    @Override
    public ItemStack getHasRarmorInSlot(Entity entity, EntityEquipmentSlot slot){
        if(entity instanceof EntityLivingBase){
            ItemStack stack = ((EntityLivingBase)entity).getItemStackFromSlot(slot);
            if(!stack.isEmpty()){
                Item item = stack.getItem();
                if(item instanceof ItemRarmor && ((ItemRarmor)item).armorType == slot){
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public IRarmorData getDataForChestplate(EntityPlayer player, boolean createIfAbsent){
        ItemStack stack = this.getHasRarmorInSlot(player, EntityEquipmentSlot.CHEST);
        if(!stack.isEmpty()){
            return this.getDataForStack(player.getEntityWorld(), stack, createIfAbsent);
        }
        return null;
    }

    @Override
    public UUID checkAndSetRarmorId(ItemStack stack, boolean createIfAbsent){
        if(stack.getItem() instanceof ItemRarmor){
            if(!stack.hasTagCompound()){
                if(createIfAbsent){
                    stack.setTagCompound(new NBTTagCompound());
                }
                else{
                    return null;
                }
            }

            NBTTagCompound compound = stack.getTagCompound();
            if(!compound.hasUniqueId("RarmorId")){
                if(createIfAbsent){
                    UUID id = UUID.randomUUID();
                    compound.setUniqueId("RarmorId", id);
                    return id;
                }
                else{
                    return null;
                }
            }
            else{
                return compound.getUniqueId("RarmorId");
            }
        }
        else{
            return null;
        }
    }

    @Override
    public void openRarmor(EntityPlayer player, int moduleId, boolean alsoSetData, boolean sendRarmorDataToClient){
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
                player.openGui(Rarmor.INSTANCE, moduleId, player.getEntityWorld(), (int)player.posX, (int)player.posY, (int)player.posZ);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void openRarmorFromClient(int moduleId, boolean alsoSetData, boolean sendRarmorDataToClient){
        PacketHandler.handler.sendToServer(new PacketOpenModule(moduleId, alsoSetData, sendRarmorDataToClient));
    }

    @Override
    public boolean mergeItemStack(Container container, ItemStack stack, int startIndexIncl, int endIndexExcl, boolean reverseDirection){
        return container instanceof ContainerRarmor && ((ContainerRarmor)container).mergeItemStack(stack, startIndexIncl, endIndexExcl, reverseDirection);
    }

    @Override
    public IRarmorData getDataForStack(World world, ItemStack stack, boolean createIfAbsent){
        UUID stackId = this.checkAndSetRarmorId(stack, !world.isRemote && createIfAbsent);
        if(stackId != null && world instanceof ServerWorld){
            Map<UUID, IRarmorData> allData = WorldData.getRarmorData((ServerWorld) world);
            IRarmorData data = allData.get(stackId);
            if(data == null){
                if(createIfAbsent){
                    data = new RarmorData(stack);

                    //main module
                    ActiveRarmorModule mainModule = Helper.initiateModuleById(ActiveModuleMain.IDENTIFIER, data);
                    mainModule.onInstalled(null);
                    data.getCurrentModules().add(mainModule);
                    //color module
                    ActiveRarmorModule colorModule = Helper.initiateModuleById(ActiveModuleColor.IDENTIFIER, data);
                    colorModule.onInstalled(null);
                    data.getCurrentModules().add(colorModule);

                    data.setDirty(false);

                    allData.put(stackId, data);
                }
            }
            else{
                ItemStack bound = data.getBoundStack();
                if(bound != null || bound != stack){
                    data.setBoundStack(stack);
                }
            }

            return data;
        }
        return null;
    }

    @Override
    public boolean compareModules(ActiveRarmorModule module, Object o){
        return o instanceof ActiveRarmorModule && ((ActiveRarmorModule)o).getIdentifier().equals(module.getIdentifier());
    }
}
