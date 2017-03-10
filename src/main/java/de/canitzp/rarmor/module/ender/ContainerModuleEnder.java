/*
 * This file ("ContainerModuleEnder.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.ender;

import de.canitzp.rarmor.CompatUtil;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ContainerModuleEnder extends RarmorModuleContainer {

    private final EntityPlayer player;

    public ContainerModuleEnder(EntityPlayer player, Container container, ActiveRarmorModule module){
        super(container, module);
        this.player = player;
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<Slot>();

        InventoryEnderChest ender = this.player.getInventoryEnderChest();
        for(int j = 0; j < 3; j++){
            for(int k = 0; k < 9; k++){
                slots.add(new Slot(ender, k+j*9, 38+k*18, 26+j*18));
            }
        }


        return slots;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        int inventoryStart = 27;
        int inventoryEnd = inventoryStart+26;
        int hotbarStart = inventoryEnd+1;
        int hotbarEnd = hotbarStart+8;

        Slot theSlot = this.actualContainer.inventorySlots.get(slot);

        if(theSlot != null && theSlot.getHasStack()){
            ItemStack newStack = theSlot.getStack();
            ItemStack currentStack = newStack.copy();

            if(slot >= inventoryStart){
                //Change things here
                if(!this.mergeItemStack(newStack, 0, 27, false)){
                    if(slot >= inventoryStart && slot <= inventoryEnd){
                        if(!this.mergeItemStack(newStack, hotbarStart, hotbarEnd+1, false)){
                            return CompatUtil.getEmpty();
                        }
                    }
                    else if(slot >= inventoryEnd+1 && slot < hotbarEnd+1 && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd+1, false)){
                        return CompatUtil.getEmpty();
                    }
                }
                //Not here anymore
            }
            else if(!this.mergeItemStack(newStack, inventoryStart, hotbarEnd+1, false)){
                return CompatUtil.getEmpty();
            }

            if(CompatUtil.getStackCount(newStack) <= 0){
                theSlot.putStack(CompatUtil.getEmpty());
            }
            else{
                theSlot.onSlotChanged();
            }

            if(CompatUtil.getStackCount(newStack) == CompatUtil.getStackCount(currentStack)){
                return CompatUtil.getEmpty();
            }
            //theSlot.onTake(player, newStack);
            theSlot.func_82870_a(player, newStack);

            return currentStack;
        }

        return CompatUtil.getEmpty();
    }
}
