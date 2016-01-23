package de.canitzp.rarmor.inventory.container.Slots;

import de.canitzp.util.util.NBTUtil;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmor;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotArmorInventory extends Slot implements ISpecialSlot{

    private final EntityPlayer player;
    public boolean slotExist = true;

    public SlotArmorInventory(IInventory inventory, int id, int x, int y, EntityPlayer player) {
        super(inventory, id, x, y);
        this.player = player;
    }

    @Override
    public void onSlotChanged(){
        ItemStack stack = this.player.getCurrentArmor(ItemRFArmor.ArmorType.BODY.getId() + 1);
        if(stack != null && stack.getItem() != null && stack.getItem() instanceof ItemRFArmorBody){
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
