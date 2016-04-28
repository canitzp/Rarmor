/*
 * This file 'SlotCraftingInput.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.api.slots.ISpecialSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * @author canitzp
 */
public class SlotCraftingInput extends Slot implements ISpecialSlot{

    public boolean isSlot;

    public SlotCraftingInput(IInventory inventoryIn, int index, int xPosition, int yPosition){
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean doesSlotExist(){
        return this.isSlot;
    }

    @Override
    public void setSlotExist(boolean b){
        this.isSlot = b;
    }
}
