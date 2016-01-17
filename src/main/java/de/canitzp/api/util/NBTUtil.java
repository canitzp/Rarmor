package de.canitzp.api.util;

import de.canitzp.api.inventory.InventoryBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * @author canitzp
 */
public class NBTUtil {

    public static void checkForNBT(ItemStack stack){
        if(stack.stackTagCompound == null){
            stack.stackTagCompound = new NBTTagCompound();
        }
    }

    public static void saveSlots(ItemStack stack, InventoryBase inventory){
        checkForNBT(stack);
        ItemStack[] slots;
        if(inventory != null){
            slots = inventory.slots;
            if(slots != null && slots.length > 0){
                stack.stackTagCompound.setInteger("SlotAmount", slots.length);
                stack.stackTagCompound.setString("InventoryName", inventory.getInventoryName());
                NBTTagList tagList = new NBTTagList();
                for(int currentIndex = 0; currentIndex < slots.length; currentIndex++){
                    if(slots[currentIndex] != null){
                        NBTTagCompound tagCompound = new NBTTagCompound();
                        tagCompound.setByte("Slot", (byte)currentIndex);
                        slots[currentIndex].writeToNBT(tagCompound);
                        tagList.appendTag(tagCompound);
                    }
                }
                stack.stackTagCompound.setTag("Items", tagList);
            }
        }
    }

    public static InventoryBase readSlots(ItemStack stack, int slotAmo){
        NBTTagCompound compound = stack.getTagCompound();
        if(compound != null) {
            if(compound.getInteger("SlotAmount") != 0){
                int slotAmount = compound.getInteger("SlotAmount");
                ItemStack[] slots = new ItemStack[slotAmount];
                if (slots.length > 0) {
                    NBTTagList tagList = compound.getTagList("Items", 10);
                    for (int i = 0; i < tagList.tagCount(); i++) {
                        NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                        byte slotIndex = tagCompound.getByte("Slot");
                        if (slotIndex >= 0 && slotIndex < slots.length) {
                            slots[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
                        }
                    }
                }
                InventoryBase inv = new InventoryBase("", slotAmount);
                inv.slots = slots;
                return inv;
            }
        }
        return new InventoryBase("", slotAmo);
    }

    public static void setInteger(ItemStack stack, String name, int i){
        checkForNBT(stack);
        stack.stackTagCompound.setInteger(name, i);
    }

    public static int getInteger(ItemStack stack, String name){
        if(stack.stackTagCompound != null){
            return stack.stackTagCompound.getInteger(name);
        } else return 0;
    }

    public static void setBoolean(ItemStack stack, String name, boolean b){
        checkForNBT(stack);
        stack.stackTagCompound.setBoolean(name, b);
    }

    public static boolean getBoolean(ItemStack stack, String name) {
        return stack.stackTagCompound != null && stack.stackTagCompound.getBoolean(name);
    }

}
