/*
 * This file ("ContainerModuleStorage.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.storage;

import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.crafting.CraftingManager;

import java.util.ArrayList;
import java.util.List;

public class ContainerModuleStorage extends RarmorModuleContainer{

    private final EntityPlayer player;

    public InventoryCrafting craftMatrix = new InventoryCrafting(this.actualContainer, 3, 3);
    public InventoryCraftResult craftResult = new InventoryCraftResult();

    public ContainerModuleStorage(EntityPlayer player, Container container, ActiveRarmorModule module, IRarmorData currentData){
        super(container, module, currentData);
        this.player = player;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory){
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.player.worldObj));
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
}
