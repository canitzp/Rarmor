/*
 * This file 'SlotInputModule.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotInputModule extends SlotUpdate{

    protected ItemStack actualStack;

    public SlotInputModule(InventoryBase inventory, int id, int x, int y, EntityPlayer player){
        super(inventory, id, x, y, player);
    }

    @Override
    public boolean isItemValid(ItemStack stack){
        return stack.getItem() instanceof IRarmorModule;
    }

    @Override
    public void onSlotChanged(){
        ItemStack stack = RarmorUtil.getPlayersRarmorChestplate(player);
        if(actualStack != null && !actualStack.isItemEqual(this.getStack())){
            if(actualStack.getItem() != null && actualStack.getItem() instanceof IRarmorModule){
                //((IRarmorModule) actualStack.getItem()).onPickupFromSlot(player.getEntityWorld(), player, RarmorUtil.getPlayersRarmorChestplate(player), stack, this.inv, this);
                this.onPickupFromSlot(player, actualStack);
            }
        }
        if(getStack() != null && getStack().getItem() != null){
            if(getStack().getItem() instanceof IRarmorModule){
                NBTUtil.setBoolean(stack, "Module" + ((IRarmorModule) getStack().getItem()).getUniqueName(), true);
            }
        }
        actualStack = this.getStack();
        super.onSlotChanged();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack){
        if(stack.getItem() instanceof IRarmorModule){
            ((IRarmorModule) stack.getItem()).onPickupFromSlot(player.getEntityWorld(), player, RarmorUtil.getPlayersRarmorChestplate(player), stack, this.inv);
            NBTUtil.setBoolean(stack, "Module" + ((IRarmorModule) stack.getItem()).getUniqueName(), false);
            NBTUtil.setBoolean(stack, "FirstOpenedModule", true);
        }
        actualStack = null;
    }

}
