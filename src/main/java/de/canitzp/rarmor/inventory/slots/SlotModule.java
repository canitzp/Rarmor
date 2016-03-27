package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.api.slots.IModuleSlot;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotModule extends SlotUpdate implements IModuleSlot{

    public boolean active;

    public SlotModule(IInventory inventory, int id, int x, int y, ItemStack saveStack) {
        super(inventory, id, x, y, saveStack);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isSlotActive() {
        return active;
    }

    @Override
    public void setSlotActive() {
        this.active = true;
    }

    @Override
    public void setSlotInactive() {
        this.active = false;
    }

}
