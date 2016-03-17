package de.canitzp.rarmor.api;

import de.canitzp.rarmor.util.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotUpdate extends Slot implements ISpecialSlot {

    private final EntityPlayer player;
    public boolean slotExist = true;

    public SlotUpdate(IInventory inventory, int id, int x, int y, EntityPlayer player) {
        super(inventory, id, x, y);
        this.player = player;
    }

    @Override
    public void onSlotChanged(){
        ItemStack stack = this.player.getCurrentArmor(2);
        if(stack != null){
            NBTUtil.saveSlots(stack, inventory);
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
