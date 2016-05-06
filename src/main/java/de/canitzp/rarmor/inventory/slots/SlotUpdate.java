/*
 * This file 'SlotUpdate.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

/**
 * @author canitzp
 */
public class SlotUpdate extends Slot{

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
