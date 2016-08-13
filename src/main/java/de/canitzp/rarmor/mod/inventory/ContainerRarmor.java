/*
 * This file ("ContainerRarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.inventory;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerRarmor extends Container{

    public final RarmorModuleContainer container;

    public ContainerRarmor(InventoryPlayer inventory, RarmorModuleContainer container){
        this.container = container;

        for(Slot slot : this.container.getSlots()){
            this.addSlotToContainer(slot);
        }
    }

    @Override
    public void addListener(IContainerListener listener){
        super.addListener(listener);
        this.container.addListener(listener);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void removeListener(IContainerListener listener){
        super.removeListener(listener);
        this.container.removeListener(listener);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn){
        return true;
    }

    @Override
    public void detectAndSendChanges(){
        super.detectAndSendChanges();

        this.container.detectAndSendChanges();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index){
        return this.container.transferStackInSlot(player, index);
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        super.onContainerClosed(player);
        this.container.onContainerClosed(player);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory){
        super.onCraftMatrixChanged(inventory);
        this.container.onCraftMatrixChanged(inventory);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data){
        super.updateProgressBar(id, data);
        this.container.updateProgressBar(id, data);
    }
}
