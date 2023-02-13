/*
 * This file ("ContainerRarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.inventory;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.inventory.slot.SlotModule;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class ContainerRarmor extends AbstractContainerMenu {
    
    public RarmorModuleContainer container;
    private IRarmorData currentData;
    //this is needed due to putStack() also being called when the container is opened
    //but only on the client for some reason. Ugh.
    public boolean isPuttingStacksInSlots;

    public static ContainerRarmor create(int windowId, Inventory inv, FriendlyByteBuf data){
        IRarmorData rarmorData = RarmorAPI.methodHandler.getDataForChestplate(inv.player, true);
        ActiveRarmorModule activeModule = rarmorData.getCurrentModules().get(rarmorData.getCurrentModules().size() <= rarmorData.getSelectedModule() ? 0 : rarmorData.getSelectedModule());
        return new ContainerRarmor(windowId, inv.player, activeModule);
    }

    public ContainerRarmor(int windowId, Player player, ActiveRarmorModule currentModule){
        super(RarmorContainerRegistry.RARMOR_CONTAINER.get(), windowId);
        this.container = currentModule.createContainer(player, this);
        this.currentData = currentModule.data;

        for(Slot slot : this.container.getSlots()){
            this.addSlot(slot);
        }

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 9; j++){
                this.addSlot(new Slot(player.getInventory(), j+i*9+9, 38+j*18, 147+i*18));
            }
        }

        for(int k = 0; k < 9; k++){
            this.addSlot(new Slot(player.getInventory(), k, 38+k*18, 205));
        }
    }

    @Override
    public void addSlotListener(ContainerListener listener){
        super.addSlotListener(listener);
        this.container.addListener(listener);
    }
    
    @Override
    public void removeSlotListener(ContainerListener listener){
        super.removeSlotListener(listener);
        this.container.removeListener(listener);
    }

    @Override
    public boolean stillValid(Player player){
        return true;
    }

    @Override
    public void broadcastChanges(){
        super.broadcastChanges();
        this.container.detectAndSendChanges();
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        this.container.onCraftMatrixChanged(container);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.onContainerClosed(player);
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index){
        return this.container.transferStackInSlot(player, index);
    }

    @Override
    public void setItem(int p_182407_, int p_182408_, ItemStack p_182409_) {
        //this.isPuttingStacksInSlots = true;
        System.out.println("Set " + p_182407_ + " to " + p_182409_);
        super.setItem(p_182407_, p_182408_, p_182409_);
        //this.isPuttingStacksInSlots = false;
    }

    @Override
    public void initializeContents(int p_182411_, List<ItemStack> p_182412_, ItemStack p_182413_) {
        //this.isPuttingStacksInSlots = true;
        System.out.println("Remote-Carried: " + p_182413_);
        super.initializeContents(p_182411_, p_182412_, p_182413_);
        //this.isPuttingStacksInSlots = false;
    }

    /*@Override
    public void clicked(int slotId, int dragType, @Nonnull ClickType clickType, @Nonnull Player player){

        ItemStack stack = this.container.slotClick(slotId, dragType, clickType, player);
        if(stack.isEmpty()){
            stack = super.clicked(slotId, dragType, clickType, player);
        }

        if(!player.getLevel().isClientSide()){
            if(slotId > 0 && slotId < this.slots.size()){
                Slot slot = this.slots.get(slotId);
                if(slot instanceof SlotModule){
                    this.currentData.queueUpdate(true, -1, true);
                }
            }
        }
    }*/

    // for MethodHandler as a pass through since the parent method is protected
    @Override
    public boolean moveItemStackTo(ItemStack p_38904_, int p_38905_, int p_38906_, boolean p_38907_) {
        return super.moveItemStackTo(p_38904_, p_38905_, p_38906_, p_38907_);
    }
}
