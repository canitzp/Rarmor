/*
 * This file 'SlotModule.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.slots.IModuleSlot;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotModule extends SlotUpdate implements IModuleSlot{

    public boolean active;

    public SlotModule(InventoryBase inventory, int id, int x, int y, EntityPlayer player){
        super(inventory, id, x, y, player);
    }

    @Override
    public boolean isItemValid(ItemStack stack){
        return true;
    }

    @Override
    public boolean isSlotActive(){
        return active;
    }

    @Override
    public void setSlotActive(){
        this.active = true;
    }

    @Override
    public void setSlotInactive(){
        this.active = false;
    }

}
