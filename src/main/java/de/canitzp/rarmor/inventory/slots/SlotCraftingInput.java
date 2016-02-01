package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.api.ISpecialSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * @author canitzp
 */
public class SlotCraftingInput extends Slot implements ISpecialSlot {

    public boolean isSlot;

    public SlotCraftingInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean doesSlotExist() {
        return this.isSlot;
    }

    @Override
    public void setSlotExist(boolean b) {
        this.isSlot = b;
    }
}
