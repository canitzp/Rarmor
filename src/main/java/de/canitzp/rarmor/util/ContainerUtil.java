package de.canitzp.rarmor.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author canitzp
 */
public class ContainerUtil {

    public static ItemStack transferStackInSlot(List<Slot> inventorySlots, EntityPlayer player, int slot) {
        final int inventoryStart = 27;
        final int inventoryEnd = inventoryStart + 26;
        final int hotbarStart = inventoryEnd + 1;
        final int hotbarEnd = hotbarStart + 8;
        Slot theSlot = inventorySlots.get(slot);
        if (theSlot != null && theSlot.getHasStack()) {
            ItemStack newStack = theSlot.getStack();
            ItemStack currentStack = newStack.copy();
            //Other Slots in Inventory excluded
            if (slot >= inventoryStart) {
                //Shift from Inventory
                if (!mergeItemStack(newStack, 0, 9, false, inventorySlots)) {
                    //
                    if (slot >= inventoryStart && slot <= inventoryEnd) {
                        if (!mergeItemStack(newStack, hotbarStart, hotbarEnd + 1, false, inventorySlots)) {
                            return null;
                        }
                    } else if (slot >= inventoryEnd + 1 && slot < hotbarEnd + 1 && !mergeItemStack(newStack, inventoryStart, inventoryEnd + 1, false, inventorySlots)) {
                        return null;
                    }
                }
            } else if (!mergeItemStack(newStack, inventoryStart, hotbarEnd + 1, false, inventorySlots)) {
                return null;
            }
            if (newStack.stackSize == 0) {
                theSlot.putStack(null);
            } else {
                theSlot.onSlotChanged();
            }
            if (newStack.stackSize == currentStack.stackSize) {
                return null;
            }
            theSlot.onPickupFromSlot(player, newStack);
            return currentStack;
        }
        return null;
    }

    public static boolean mergeItemStack(ItemStack stack, int p_75135_2_, int p_75135_3_, boolean p_75135_4_, List<Slot> inventorySlots) {
        boolean flag1 = false;
        int k = p_75135_2_;
        if (p_75135_4_) {
            k = p_75135_3_ - 1;
        }
        Slot slot;
        ItemStack itemstack1;
        if (stack.isStackable()) {
            while (stack.stackSize > 0 && (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_)) {
                slot = inventorySlots.get(k);
                itemstack1 = slot.getStack();
                if (itemstack1 != null && itemstack1.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, itemstack1)) {
                    int l = itemstack1.stackSize + stack.stackSize;
                    if (l <= stack.getMaxStackSize()) {
                        stack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    } else if (itemstack1.stackSize < stack.getMaxStackSize()) {
                        stack.stackSize -= stack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = stack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }
                if (p_75135_4_) {
                    --k;
                } else {
                    ++k;
                }
            }
        }
        if (stack.stackSize > 0) {
            if (p_75135_4_) {
                k = p_75135_3_ - 1;
            } else {
                k = p_75135_2_;
            }
            while (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_) {
                slot = inventorySlots.get(k);
                itemstack1 = slot.getStack();
                if (itemstack1 == null) {
                    slot.putStack(stack.copy());
                    slot.onSlotChanged();
                    stack.stackSize = 0;
                    flag1 = true;
                    break;
                }
                if (p_75135_4_) {
                    --k;
                } else {
                    ++k;
                }
            }
        }
        return flag1;
    }

}
