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
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ContainerModuleStorage extends RarmorModuleContainer {

    private final Player player;

    public CraftingContainer craftMatrix = new CraftingContainer(this.actualContainer, 3, 3);
    public ResultContainer craftResult = new ResultContainer();

    public ContainerModuleStorage(Player player, AbstractContainerMenu container, ActiveRarmorModule module){
        super(container, module);
        this.player = player;
    }

    @Override
    public void onCraftMatrixChanged(Container inventory){
        CraftingRecipe recipe = this.player.level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, this.craftMatrix, this.player.level).orElse(null);
        if(recipe != null){
            this.craftResult.setItem(0, recipe.getResultItem());
        }
    }

    @Override
    public void onContainerClosed(Player player){
        ActiveModuleStorage module = (ActiveModuleStorage)this.module;
        for(int i = 0; i < this.craftMatrix.getContainerSize(); i++){
            module.inventory.setStackInSlot(i+36, this.craftMatrix.getItem(i));
        }
        module.inventory.setStackInSlot(45, this.craftResult.getItem(0));
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<>();

        ActiveModuleStorage module = (ActiveModuleStorage)this.module;
        for(int j = 0; j < 4; j++){
            for(int k = 0; k < 9; k++){
                slots.add(new SlotItemHandler(module.inventory, k+j*9, 10+k*18, 16+j*18));
            }
        }

        slots.add(new ResultSlot(this.player, this.craftMatrix, this.craftResult, 0, 195, 99));
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                slots.add(new Slot(this.craftMatrix, j+i*3, 176+j*18, 25+i*18));
            }
        }

        for(int i = 0; i < this.craftMatrix.getContainerSize(); i++){
            this.craftMatrix.setItem(i, module.inventory.getStackInSlot(i+36));
        }
        this.craftResult.setItem(0, module.inventory.getStackInSlot(45));

        return slots;
    }

    @Override
    public ItemStack transferStackInSlot(Player player, int slot){
        int inventoryStart = 46;
        int inventoryEnd = inventoryStart+26;
        int hotbarStart = inventoryEnd+1;
        int hotbarEnd = hotbarStart+8;

        Slot theSlot = this.actualContainer.slots.get(slot);

        if(theSlot != null && theSlot.hasItem()){
            ItemStack newStack = theSlot.getItem();
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
