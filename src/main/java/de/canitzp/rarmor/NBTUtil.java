package de.canitzp.rarmor;

import de.canitzp.rarmor.armor.RarmorColoringTab;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * @author canitzp
 */
public class NBTUtil{

    public static void setInteger(ItemStack stack, int i, String key){
        if(stack != null){
            if(stack.hasTagCompound()){
                stack.getTagCompound().setInteger(key, i);
            } else {
                stack.setTagCompound(new NBTTagCompound());
                setInteger(stack, i, key);
            }
        }
    }

    public static int getInteger(ItemStack stack, String key){
        if(stack != null && stack.hasTagCompound()){
            return stack.getTagCompound().getInteger(key);
        }
        return 0;
    }

    public static void setEnergy(ItemStack stack, int energy){
        setInteger(stack, energy, "Energy");
    }

    public static int getEnergy(ItemStack stack){
        return getInteger(stack, "Energy");
    }

    public static NBTTagCompound getTagFromStack(ItemStack stack){
        if(stack != null){
            if(!stack.hasTagCompound()){
                stack.setTagCompound(new NBTTagCompound());
            }
            return stack.getTagCompound();
        }
        return null;
    }

    public static void setTagFromStack(ItemStack stack, NBTTagCompound nbt){
        if(stack != null && nbt != null){
            stack.setTagCompound(nbt);
        }
    }

    public static NBTTagCompound writeInventory(NBTTagCompound toWriteOn, IInventory invToWrite){
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();
        if(invToWrite != null){
            for (int i = 0; i < invToWrite.getSizeInventory(); ++i){
                ItemStack stack = invToWrite.getStackInSlot(i);
                if (stack != null){
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setByte("Slot", (byte)i);
                    stack.writeToNBT(nbttagcompound);
                    nbttaglist.appendTag(nbttagcompound);
                }
            }
            compound.setTag("Items", nbttaglist);
            compound.setString("InvName", invToWrite.getName());
            compound.setInteger("InvAmount", invToWrite.getSizeInventory());
            toWriteOn.setTag("Inventory " + invToWrite.getName(), compound);
        }
        return toWriteOn;
    }

    private static InventoryBasic readInventory(NBTTagCompound toReadFrom, String invName){
        if(toReadFrom.hasKey("Inventory " + invName)){
            NBTTagCompound compound = toReadFrom.getCompoundTag("Inventory " + invName);
            if(compound.hasKey("InvName")){
                NBTTagList nbttaglist = compound.getTagList("Items", 10);
                InventoryBasic inv = new InventoryBasic(compound.getString("InvName"), false, compound.getInteger("InvAmount"));
                for (int i = 0; i < nbttaglist.tagCount(); ++i){
                    NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                    int j = nbttagcompound.getByte("Slot") & 255;
                    if (j >= 0 && j < inv.getSizeInventory()){
                        inv.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
                    }
                }
                return inv;
            }
        }
        return null;
    }

    public static InventoryBasic readInventory(NBTTagCompound toReadFrom, InventoryBasic oldInv){
        InventoryBasic inv = readInventory(toReadFrom, oldInv.getName());
        if(inv != null){
            return inv;
        }
        return new InventoryBasic(oldInv.getName(), oldInv.hasCustomName(), oldInv.getSizeInventory());
    }

    public static void setColor(ItemStack stack, RarmorColoringTab.Color color){
        if(stack != null){
            if(!stack.hasTagCompound()){
                stack.setTagCompound(new NBTTagCompound());
            }
            stack.getTagCompound().setInteger("ColorCode", color.hexValue);
            stack.getTagCompound().setString("ColorName", color.showName);
        }
    }

    public static int getColor(ItemStack stack){
        if(stack != null){
            if(stack.hasTagCompound()){
                if(stack.getTagCompound().hasKey("ColorCode")){
                    return stack.getTagCompound().getInteger("ColorCode");
                }
            }
        }
        return 0xFFFFFF; // White/No Color
    }

}
