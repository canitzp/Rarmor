package de.canitzp.rarmor.api.slots;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotUpdate extends Slot implements ISpecialSlot {

    private boolean slotExist = true;
    private ItemStack toSave;
    protected InventoryBase inventory;

    public SlotUpdate(InventoryBase inventory, int id, int x, int y, ItemStack saveStack) {
        super(inventory, id, x, y);
        this.toSave = saveStack;
        this.inventory = inventory;
    }

    @Override
    public void onSlotChanged() {
        if (toSave != null) {
            NBTUtil.saveSlots(toSave, inventory);
        }
        super.onSlotChanged();
    }

    @Override
    public boolean doesSlotExist() {
        return this.slotExist;
    }

    @Override
    public void setSlotExist(boolean b) {
        this.slotExist = b;
    }
}
