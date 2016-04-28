/*
 * This file 'SlotUnmovable.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.inventory.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotUnmovable extends Slot{

    public SlotUnmovable(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_){
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Override
    public boolean isItemValid(ItemStack stack){
        return false;
    }

    @Override
    public void putStack(ItemStack stack){

    }

    @Override
    public ItemStack decrStackSize(int i){
        return null;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player){
        return false;
    }
}
