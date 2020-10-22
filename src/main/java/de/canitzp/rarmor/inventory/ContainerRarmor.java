/*
 * This file ("ContainerRarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.inventory;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.inventory.slot.SlotModule;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

public class ContainerRarmor extends Container {
    
    public RarmorModuleContainer container;
    private IRarmorData currentData;
    //this is needed due to putStack() also being called when the container is opened
    //but only on the client for some reason. Ugh.
    public boolean isPuttingStacksInSlots;

    public ContainerRarmor(int windowId, PlayerInventory playerInventory){
        super(ContainerTypes.RARMOR_CONTAINER.get(), windowId);
    }
    
    public ContainerRarmor(int windowId, PlayerEntity player, ActiveRarmorModule currentModule){
        super(ContainerTypes.RARMOR_CONTAINER.get(), windowId);
        this.container = currentModule.createContainer(player, this);
        this.currentData = currentModule.data;

        for(Slot slot : this.container.getSlots()){
            this.addSlot(slot);
        }

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 9; j++){
                this.addSlot(new Slot(player.inventory, j+i*9+9, 38+j*18, 147+i*18));
            }
        }

        for(int k = 0; k < 9; k++){
            this.addSlot(new Slot(player.inventory, k, 38+k*18, 205));
        }
    }
    
    @Override
    public void addListener(IContainerListener listener){
        super.addListener(listener);
        this.container.addListener(listener);
    }
    
    @Override
    public void removeListener(IContainerListener listener){
        super.removeListener(listener);
        this.container.removeListener(listener);
    }

    @Override
    public boolean canInteractWith(PlayerEntity player){
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void setAll(List<ItemStack> stack){
        this.isPuttingStacksInSlots = true;
        super.setAll(stack);
        this.isPuttingStacksInSlots = false;
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack){
        this.isPuttingStacksInSlots = true;
        super.putStackInSlot(slotID, stack);
        this.isPuttingStacksInSlots = false;
    }

    @Override
    public void detectAndSendChanges(){
        super.detectAndSendChanges();
        this.container.detectAndSendChanges();
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index){
        return this.container.transferStackInSlot(player, index);
    }

    @Override
    public void onContainerClosed(@Nonnull PlayerEntity player){
        super.onContainerClosed(player);
        this.container.onContainerClosed(player);
    }

    @Override
    public void onCraftMatrixChanged(@Nonnull IInventory inventory){
        super.onCraftMatrixChanged(inventory);
        this.container.onCraftMatrixChanged(inventory);
    }

    @Override
    public void updateProgressBar(int id, int data){
        super.updateProgressBar(id, data);
        this.container.updateProgressBar(id, data);
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, @Nonnull ClickType clickType, @Nonnull PlayerEntity player){
        ItemStack stack = this.container.slotClick(slotId, dragType, clickType, player);
        if(stack.isEmpty()){
            stack = super.slotClick(slotId, dragType, clickType, player);
        }

        if(!player.getEntityWorld().isRemote){
            if(slotId > 0 && slotId < this.inventorySlots.size()){
                Slot slot = this.inventorySlots.get(slotId);
                if(slot instanceof SlotModule){
                    this.currentData.queueUpdate(true, -1, true);
                }
            }
        }

        return stack;
    }

    @Override
    public boolean mergeItemStack(@Nonnull ItemStack stack, int startIndex, int endIndex, boolean reverseDirection){
        return super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
    }
}
