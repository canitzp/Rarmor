/*
 * This file 'NBTUtil.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.util;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.InventoryBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * @author canitzp
 */
public class NBTUtil{

    public static void checkForNBT(ItemStack stack){
        if(stack != null){
            if(stack.getTagCompound() == null){
                stack.setTagCompound(new NBTTagCompound());
            }
        } else {
            Rarmor.logger.error("An Error occurred while writing/reading a ItemStack");
            stack = new ItemStack(Blocks.FIRE);
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    public static void saveSlots(ItemStack stack, IInventory inventory){
        checkForNBT(stack);
        if(inventory != null && inventory.getSizeInventory() > 0){
            stack.getTagCompound().setString("InventoryName", inventory.getName());
            NBTTagList tagList = new NBTTagList();
            for(int currentIndex = 0; currentIndex < inventory.getSizeInventory(); currentIndex++){
                if(inventory.getStackInSlot(currentIndex) != null){
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setByte("Slot", (byte) currentIndex);
                    inventory.getStackInSlot(currentIndex).writeToNBT(tagCompound);
                    tagList.appendTag(tagCompound);
                }
            }
            stack.getTagCompound().setTag("Items", tagList);
        }
    }

    public static InventoryBase readSlots(ItemStack stack, int slotAmo){
        NBTTagCompound compound = stack.getTagCompound();
        if(compound != null){
            if(slotAmo != 0){
                String name = compound.getString("InventoryName");
                InventoryBase inv = new InventoryBase(name, slotAmo);
                if(inv.getSizeInventory() > 0){
                    NBTTagList tagList = compound.getTagList("Items", 10);
                    for(int i = 0; i < tagList.tagCount(); i++){
                        NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                        byte slotIndex = tagCompound.getByte("Slot");
                        if(slotIndex >= 0 && slotIndex < inv.getSizeInventory()){
                            inv.setInventorySlotContents(slotIndex, ItemStack.loadItemStackFromNBT(tagCompound));
                        }
                    }
                }
                return inv;
            }
        }
        return new InventoryBase("", slotAmo);
    }

    public static void setInteger(ItemStack stack, String name, int i){
        checkForNBT(stack);
        stack.getTagCompound().setInteger(name, i);
    }

    public static void setIntegerIfNot(ItemStack stack, String name, int i){
        checkForNBT(stack);
        if(stack.getTagCompound().getInteger(name) != i){
            stack.getTagCompound().setInteger(name, i);
        }
    }

    public static int getInteger(ItemStack stack, String name){
        if(stack.getTagCompound() != null && stack.getTagCompound().hasKey(name)){
            return stack.getTagCompound().getInteger(name);
        } else return 0;
    }

    public static void setBoolean(ItemStack stack, String name, boolean b){
        checkForNBT(stack);
        stack.getTagCompound().setBoolean(name, b);
    }

    public static boolean getBoolean(ItemStack stack, String name){
        return stack.getTagCompound() != null && stack.getTagCompound().hasKey(name) && stack.getTagCompound().getBoolean(name);
    }

    public static void setString(ItemStack stack, String key, String value){
        checkForNBT(stack);
        stack.getTagCompound().setString(key, value);
    }

    public static String getString(ItemStack stack, String key){
        return stack.getTagCompound() != null ? stack.getTagCompound().getString(key) : null;
    }

}
