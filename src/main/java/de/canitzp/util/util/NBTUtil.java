package de.canitzp.util.util;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.util.inventory.InventoryBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * @author canitzp
 */
public class NBTUtil {

    public static void checkForNBT(ItemStack stack){
        if(stack != null){
            if(stack.getTagCompound() == null){
                stack.setTagCompound(new NBTTagCompound());
            }
        } else {
            Rarmor.logger.error("YOUR ITEMSTACK IS NULL! THIS CAN CAUSE CRASHES! NBTUtil-checkForNBT");
        }
    }

    public static void saveSlots(ItemStack stack, IInventory inventory){
        checkForNBT(stack);
        if(inventory != null && inventory.getSizeInventory() > 0){
                stack.getTagCompound().setInteger("SlotAmount", inventory.getSizeInventory());
                stack.getTagCompound().setString("InventoryName", inventory.getName());
                NBTTagList tagList = new NBTTagList();
                for(int currentIndex = 0; currentIndex < inventory.getSizeInventory(); currentIndex++){
                    if(inventory.getStackInSlot(currentIndex) != null){
                        NBTTagCompound tagCompound = new NBTTagCompound();
                        tagCompound.setByte("Slot", (byte)currentIndex);
                        inventory.getStackInSlot(currentIndex).writeToNBT(tagCompound);
                        tagList.appendTag(tagCompound);
                    }
                }
                stack.getTagCompound().setTag("Items", tagList);
        }
    }

    public static InventoryBasic readSlots(ItemStack stack, int slotAmo){
        NBTTagCompound compound = stack.getTagCompound();
        if(compound != null) {
            if(compound.getInteger("SlotAmount") != 0){
                int slotAmount = compound.getInteger("SlotAmount");
                InventoryBasic inv = new InventoryBasic("", false, slotAmount);
                if (inv.getSizeInventory() > 0) {
                    NBTTagList tagList = compound.getTagList("Items", 10);
                    for (int i = 0; i < tagList.tagCount(); i++) {
                        NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                        byte slotIndex = tagCompound.getByte("Slot");
                        if (slotIndex >= 0 && slotIndex < inv.getSizeInventory()) {
                            inv.setInventorySlotContents(slotIndex, ItemStack.loadItemStackFromNBT(tagCompound));
                        }
                    }
                }
                return inv;
            }
        }
        return new InventoryBasic("", false, slotAmo);
    }

    public static InventoryBase readSlotsBase(ItemStack stack, int slotAmo){
        NBTTagCompound compound = stack.getTagCompound();
        if(compound != null) {
            if(compound.getInteger("SlotAmount") != 0){
                int slotAmount = compound.getInteger("SlotAmount");
                InventoryBase inv = new InventoryBase("", slotAmount);
                if (inv.getSizeInventory() > 0) {
                    NBTTagList tagList = compound.getTagList("Items", 10);
                    for (int i = 0; i < tagList.tagCount(); i++) {
                        NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                        byte slotIndex = tagCompound.getByte("Slot");
                        if (slotIndex >= 0 && slotIndex < inv.getSizeInventory()) {
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
        if(stack.getTagCompound() != null){
            return stack.getTagCompound().getInteger(name);
        } else return 0;
    }

    public static void setBoolean(ItemStack stack, String name, boolean b){
        if(stack != null){
            checkForNBT(stack);
            stack.getTagCompound().setBoolean(name, b);
        } else {
            Rarmor.logger.error("NBTUtil.setBoolean(): My ItemStack is null. THIS IS AN ERROR!");
        }

    }

    public static boolean getBoolean(ItemStack stack, String name) {
        return stack.getTagCompound() != null && stack.getTagCompound().getBoolean(name);
    }

}
