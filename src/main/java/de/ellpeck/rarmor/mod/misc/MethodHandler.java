/*
 * This file ("MethodHand.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.misc;

import de.ellpeck.rarmor.api.internal.IMethodHandler;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.data.RarmorData;
import de.ellpeck.rarmor.mod.module.main.ActiveModuleMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;

public class MethodHandler implements IMethodHandler{

    @Override
    public IRarmorData getDataForChestplate(EntityPlayer player, boolean createIfAbsent){
        ItemStack stack = player.inventory.armorInventory[EntityEquipmentSlot.CHEST.getIndex()];
        if(stack != null){
            return this.getDataForStack(player.worldObj, stack, createIfAbsent);
        }
        return null;
    }

    @Override
    public UUID checkAndSetRarmorId(ItemStack stack, boolean create){
        if(!stack.hasTagCompound()){
            if(create){
                stack.setTagCompound(new NBTTagCompound());
            }
            else{
                return null;
            }
        }

        NBTTagCompound compound = stack.getTagCompound();
        if(!compound.hasUniqueId("RarmorId")){
            if(create){
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

    @Override
    public IRarmorData getDataForStack(World world, ItemStack stack, boolean createIfAbsent){
        UUID stackId = this.checkAndSetRarmorId(stack, !world.isRemote && createIfAbsent);
        if(stackId != null){
            Map<UUID, IRarmorData> allData = RarmorData.getRarmorData(world.isRemote);
            IRarmorData data = allData.get(stackId);
            if(data == null){
                if(createIfAbsent){
                    data = new RarmorData(stack);

                    NBTTagCompound storedData = null;
                    if(stack.hasTagCompound()){
                        NBTTagCompound compound = stack.getTagCompound();
                        if(compound.hasKey("RarmorData")){
                            storedData = compound.getCompoundTag("RarmorData");
                        }
                    }

                    if(storedData == null){
                        ActiveRarmorModule module = Helper.initiateModuleById(ActiveModuleMain.IDENTIFIER, data);
                        module.onInstalled(null);
                        data.getCurrentModules().add(module);
                    }
                    else{
                        data.readFromNBT(storedData, false);
                    }

                    allData.put(stackId, data);
                }
            }
            else if(data.getBoundStack() != stack){
                data.setBoundStack(stack);
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
