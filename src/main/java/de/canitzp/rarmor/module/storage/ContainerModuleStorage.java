/*
 * This file ("ContainerModuleStorage.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.storage;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

import java.util.ArrayList;
import java.util.List;

public class ContainerModuleStorage extends RarmorModuleContainer {

    private final EntityPlayer player;

    public InventoryCrafting craftMatrix = new InventoryCrafting(this.actualContainer, 3, 3);
    public InventoryCraftResult craftResult = new InventoryCraftResult();

    public ContainerModuleStorage(EntityPlayer player, Container container, ActiveRarmorModule module){
        super(container, module);
        this.player = player;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory){
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.player.getEntityWorld()));
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        ActiveModuleStorage module = (ActiveModuleStorage)this.module;
        for(int i = 0; i < this.craftMatrix.getSizeInventory(); i++){
            module.inventory.setInventorySlotContents(i+36, this.craftMatrix.getStackInSlot(i));
        }
        module.inventory.setInventorySlotContents(45, this.craftResult.getStackInSlot(0));
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<Slot>();

        ActiveModuleStorage module = (ActiveModuleStorage)this.module;
        for(int j = 0; j < 4; j++){
            for(int k = 0; k < 9; k++){
                slots.add(new Slot(module.inventory, k+j*9, 10+k*18, 16+j*18));
            }
        }

        slots.add(new SlotCrafting(this.player, this.craftMatrix, this.craftResult, 0, 195, 99));
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                slots.add(new Slot(this.craftMatrix, j+i*3, 176+j*18, 25+i*18));
            }
        }

        for(int i = 0; i < this.craftMatrix.getSizeInventory(); i++){
            this.craftMatrix.setInventorySlotContents(i, module.inventory.getStackInSlot(i+36));
        }
        this.craftResult.setInventorySlotContents(0, module.inventory.getStackInSlot(45));

        return slots;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        int inventoryStart = 46;
        int inventoryEnd = inventoryStart+26;
        int hotbarStart = inventoryEnd+1;
        int hotbarEnd = hotbarStart+8;

        Slot theSlot = this.actualContainer.inventorySlots.get(slot);

        if(theSlot != null && theSlot.getHasStack()){
            ItemStack newStack = theSlot.getStack();
            ItemStack currentStack = newStack.copy();

            if(slot >= inventoryStart){
                //Change things here
                if(!this.mergeItemStack(newStack, 0, 46, false)){
                    if(slot >= inventoryStart && slot <= inventoryEnd){
                        if(!this.mergeItemStack(newStack, hotbarStart, hotbarEnd+1, false)){
                            return ItemStack.EMPTY;
                        }
                    }
                    else if(slot >= inventoryEnd+1 && slot < hotbarEnd+1 && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd+1, false)){
                        return ItemStack.EMPTY;
                    }
                }
                //Not here anymore
            }
            else if(!this.mergeItemStack(newStack, inventoryStart, hotbarEnd+1, false)){
                return ItemStack.EMPTY;
            }

            if(newStack.getCount() <= 0){
                theSlot.putStack(ItemStack.EMPTY);
            }
            else{
                theSlot.onSlotChanged();
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
