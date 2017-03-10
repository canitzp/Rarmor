/*
 * This file ("ContainerModuleGenerator.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.generator;

import de.canitzp.rarmor.CompatUtil;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import java.util.Collections;
import java.util.List;

public class ContainerModuleGenerator extends RarmorModuleContainer {

    public ContainerModuleGenerator(Container container, ActiveRarmorModule module){
        super(container, module);
    }

    @Override
    public List<Slot> getSlots(){
        return Collections.singletonList(new Slot(((ActiveModuleGenerator)this.module).inventory, 0, 110, 65));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        int inventoryStart = 1;
        int inventoryEnd = inventoryStart+26;
        int hotbarStart = inventoryEnd+1;
        int hotbarEnd = hotbarStart+8;

        Slot theSlot = this.actualContainer.inventorySlots.get(slot);

        if(theSlot != null && theSlot.getHasStack()){
            ItemStack newStack = theSlot.getStack();
            ItemStack currentStack = newStack.copy();

            if(slot >= inventoryStart){
                //Change things here
                if(TileEntityFurnace.getItemBurnTime(newStack) > 0){
                    if(!this.mergeItemStack(newStack, 0, 1, false)){
                        return CompatUtil.getEmpty();
                    }
                }
                //Not here anymore
                else if(slot >= inventoryStart && slot <= inventoryEnd){
                    if(!this.mergeItemStack(newStack, hotbarStart, hotbarEnd+1, false)){
                        return CompatUtil.getEmpty();
                    }
                }
                else if(slot >= inventoryEnd+1 && slot < hotbarEnd+1 && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd+1, false)){
                    return CompatUtil.getEmpty();
                }
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
            CompatUtil.onTakeSlot(theSlot, player, newStack);

            return currentStack;
        }

        return CompatUtil.getEmpty();
    }
}
