package de.canitzp.rarmor.util;

import de.canitzp.rarmor.api.InventoryBase;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class ItemStackUtil {

    public static InventoryBase reduceStackSize(InventoryBase inventory, int slotId) {
        if (inventory.getStackInSlot(slotId) != null) {
            if (inventory.getStackInSlot(slotId).stackSize > 1) {
                inventory.getStackInSlot(slotId).stackSize--;
            } else {
                inventory.setInventorySlotContents(slotId, null);
            }
        }
        return inventory;
    }

    public static InventoryBase addStackToSlot(InventoryBase inventory, ItemStack stack, int slotId) {
        if (inventory.getStackInSlot(slotId) != null) {
            if (inventory.getStackInSlot(slotId).copy().isItemEqual(stack)) {
                inventory.setInventorySlotContents(slotId, new ItemStack(stack.getItem(), inventory.getStackInSlot(slotId).copy().stackSize + stack.stackSize, stack.getItemDamage()));
            }
        } else {
            inventory.setInventorySlotContents(slotId, stack);
        }
        return inventory;
    }

}
