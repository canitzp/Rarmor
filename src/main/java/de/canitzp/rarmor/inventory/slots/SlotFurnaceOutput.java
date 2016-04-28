/*
 * This file 'SlotFurnaceOutput.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotFurnaceOutput extends SlotUpdate{

    public SlotFurnaceOutput(InventoryBase inventory, int id, int x, int y, EntityPlayer player){
        super(inventory, id, x, y, player);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack p_75214_1_){
        return false;
    }

}
