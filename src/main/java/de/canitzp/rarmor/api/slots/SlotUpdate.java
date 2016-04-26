package de.canitzp.rarmor.api.slots;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

/**
 * @author canitzp
 */
public class SlotUpdate extends Slot {

    public InventoryBase inv;
    public EntityPlayer player;

    public SlotUpdate(InventoryBase inventory, int id, int x, int y, EntityPlayer player){
        super(inventory, id, x, y);
        this.inv = inventory;
        this.player = player;
    }

    @Override
    public void onSlotChanged(){
        RarmorUtil.saveRarmor(this.player, this.inv);
        super.onSlotChanged();
    }

}
