/*
 * This file ("ContainerModuleGenerator.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.generator;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Collections;
import java.util.List;

public class ContainerModuleGenerator extends RarmorModuleContainer {

    public ContainerModuleGenerator(AbstractContainerMenu container, ActiveRarmorModule module){
        super(container, module);
    }

    @Override
    public List<Slot> getSlots(){
        return Collections.singletonList(new SlotItemHandler(((ActiveModuleGenerator)this.module).inventory, 0, 110, 65));
    }

    @Override
    public ItemStack transferStackInSlot(Player player, int slot){
        int inventoryStart = 1;
        int inventoryEnd = inventoryStart+26;
        int hotbarStart = inventoryEnd+1;
        int hotbarEnd = hotbarStart+8;

        Slot theSlot = this.actualContainer.slots.get(slot);

        if(theSlot != null && theSlot.hasItem()){
            ItemStack newStack = theSlot.getItem();
            ItemStack currentStack = newStack.copy();

            if(slot >= inventoryStart){
                //Change things here
                if(ForgeHooks.getBurnTime(newStack, RecipeType.SMELTING) > 0){
                    if(!this.mergeItemStack(newStack, 0, 1, false)){
                        return ItemStack.EMPTY;
                    }
                }
                //Not here anymore
                else if(slot >= inventoryStart && slot <= inventoryEnd){
                    if(!this.mergeItemStack(newStack, hotbarStart, hotbarEnd+1, false)){
                        return ItemStack.EMPTY;
                    }
                }
                else if(slot >= inventoryEnd+1 && slot < hotbarEnd+1 && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd+1, false)){
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(newStack, inventoryStart, hotbarEnd+1, false)){
                return ItemStack.EMPTY;
            }

            if(newStack.getCount() <= 0){
                theSlot.set(ItemStack.EMPTY);
            }
            else{
                theSlot.setChanged();
            }

            if(newStack.getCount() == currentStack.getCount()){
                return ItemStack.EMPTY;
            }
            theSlot.onTake(player, newStack);

            return currentStack;
        }

        return ItemStack.EMPTY;
    }
}
