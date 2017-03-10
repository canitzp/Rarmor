/*
 * This file ("ContainerModuleFurnace.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.furnace;

import de.canitzp.rarmor.CompatUtil;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.ArrayList;
import java.util.List;

public class ContainerModuleFurnace extends RarmorModuleContainer {

    private final EntityPlayer player;

    public ContainerModuleFurnace(EntityPlayer player, Container container, ActiveRarmorModule module){
        super(container, module);
        this.player = player;
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<Slot>();

        ActiveModuleFurnace module = (ActiveModuleFurnace)this.module;
        slots.add(new Slot(module.inventory, 0, 82, 58));
        slots.add(new SlotFurnaceOutput(this.player, module.inventory, 1, 132, 58));

        return slots;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        int inventoryStart = 2;
        int inventoryEnd = inventoryStart+26;
        int hotbarStart = inventoryEnd+1;
        int hotbarEnd = hotbarStart+8;

        Slot theSlot = this.actualContainer.inventorySlots.get(slot);

        if(theSlot != null && theSlot.getHasStack()){
            ItemStack newStack = theSlot.getStack();
            ItemStack currentStack = newStack.copy();

            if(slot >= inventoryStart){
                //Change things here
                if(!CompatUtil.isEmpty(FurnaceRecipes.instance().getSmeltingResult(newStack))){
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
            //theSlot.onTake(player, newStack);
            theSlot.func_82870_a(player, newStack);

            return currentStack;
        }

        return CompatUtil.getEmpty();
    }
}
