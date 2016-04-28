/*
 * This file 'SlotUtil.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.util;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * @author canitzp
 */
public class SlotUtil{

    public static Slot getSlotAtPosition(GuiContainer gui, int x, int y){
        for(int k = 0; k < gui.inventorySlots.inventorySlots.size(); ++k){
            Slot slot = gui.inventorySlots.inventorySlots.get(k);
            if(x >= slot.xDisplayPosition - 1 && x < slot.xDisplayPosition + 16 + 1 && slot.yDisplayPosition >= y - 1 && slot.yDisplayPosition < y + 16 + 1){
                return slot;
            }
        }
        return null;
    }

    public static void clearSlot(IInventory inventory, int slotId){
        if(inventory != null){
            inventory.setInventorySlotContents(slotId, null);
        }
    }


}
