package de.canitzp.rarmor.util;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

/**
 * @author canitzp
 */
public class SlotUtil {

    public static Slot getSlotAtPosition(GuiContainer gui, int x, int y) {
        for (int k = 0; k < gui.inventorySlots.inventorySlots.size(); ++k) {
            Slot slot = gui.inventorySlots.inventorySlots.get(k);
            if (x >= slot.xDisplayPosition - 1 && x < slot.xDisplayPosition + 16 + 1 && slot.yDisplayPosition >= y - 1 && slot.yDisplayPosition < y + 16 + 1) {
                return slot;
            }
        }
        return null;
    }


}
