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
import de.ellpeck.rarmor.mod.item.ItemRarmor;
import de.ellpeck.rarmor.mod.module.main.ActiveModuleMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;

public class MethodHandler implements IMethodHandler{

    @Override
    public ItemStack getHasRarmorInSlot(Entity entity, EntityEquipmentSlot slot){
        if(entity instanceof EntityLivingBase){
            ItemStack stack = ((EntityLivingBase)entity).getItemStackFromSlot(slot);
            if(stack != null){
                Item item = stack.getItem();
                if(item instanceof ItemRarmor && ((ItemRarmor)item).armorType == slot){
                    return stack;
                }
            }
        }
        return null;
    }

    @Override
    public IRarmorData getDataForChestplate(EntityPlayer player, boolean createIfAbsent){
        ItemStack stack = this.getHasRarmorInSlot(player, EntityEquipmentSlot.CHEST);
        if(stack != null){
            return this.getDataForStack(player.worldObj, stack, createIfAbsent);
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
    public IRarmorData getDataForStack(World world, ItemStack stack, boolean createIfAbsent){
        UUID stackId = this.checkAndSetRarmorId(stack, !world.isRemote && createIfAbsent);
        if(stackId != null){
            Map<UUID, IRarmorData> allData = RarmorData.getRarmorData(world.isRemote);
            IRarmorData data = allData.get(stackId);
            if(data == null){
                if(createIfAbsent){
                    data = new RarmorData(stack);

                    NBTTagCompound storedData = null;
                    if(!world.isRemote){
                        if(stack.hasTagCompound()){
                            NBTTagCompound compound = stack.getTagCompound();
                            if(compound.hasKey("RarmorData")){
                                storedData = compound.getCompoundTag("RarmorData");
                                data.setDeleteStackDataOnFetch(true);
                            }
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
            else{
                if(data.getBoundStack() != stack){
                    data.setBoundStack(stack);
                }
                if(!world.isRemote && data.getDeleteStackDataOnFetch()){
                    if(stack.hasTagCompound()){
                        stack.getTagCompound().removeTag("RarmorData");
                    }
                    data.setDeleteStackDataOnFetch(false);
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
