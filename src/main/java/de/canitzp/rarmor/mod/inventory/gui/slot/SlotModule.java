/*
 * This file ("SlotModule.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.inventory.gui.slot;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.api.module.IRarmorModuleItem;
import de.canitzp.rarmor.mod.inventory.ContainerRarmor;
import de.canitzp.rarmor.mod.misc.Helper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotModule extends Slot{

    private final IRarmorData currentData;
    private final EntityPlayer player;
    private final ContainerRarmor container;

    public SlotModule(IInventory inventory, EntityPlayer player, IRarmorData currentData, ContainerRarmor container, int index, int xPosition, int yPosition){
        super(inventory, index, xPosition, yPosition);
        this.player = player;
        this.currentData = currentData;
        this.container = container;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack someStack){
        super.onPickupFromSlot(player, someStack);

        ItemStack stack = this.getStack();
        if(stack == null || stack.stackSize <= 0){
            this.uninstallModule();
        }
    }

    @Override
    public void putStack(ItemStack newStack){
        if(!this.container.isPuttingStacksInSlots){
            ItemStack stack = this.getStack();
            if(stack == null || stack.stackSize <= 0){
                this.uninstallModule();
            }
        }

        super.putStack(newStack);

        if(!this.container.isPuttingStacksInSlots){
            if(newStack != null){
                this.installModule(newStack);
            }
        }
    }

    @Override
    public ItemStack decrStackSize(int amount){
        ItemStack toReturn = super.decrStackSize(amount);

        ItemStack stack = this.getStack();
        if(stack == null || stack.stackSize <= 0){
            this.uninstallModule();
        }

        return toReturn;
    }

    private void installModule(ItemStack stack){
        if(stack.getItem() instanceof IRarmorModuleItem){
            ActiveRarmorModule module = Helper.initiateModuleById(((IRarmorModuleItem)stack.getItem()).getModuleIdentifier());
            if(module != null && !this.currentData.getCurrentModules().contains(module)){
                module.onInstalled(this.player);
                this.currentData.getCurrentModules().add(module);
                this.currentData.getSlotToModuleMap().put(this.getSlotIndex(), this.currentData.getCurrentModules().indexOf(module));
            }
        }
    }

    private void uninstallModule(){
        ActiveRarmorModule module = this.getActiveModule();
        if(module != null && this.currentData.getCurrentModules().contains(module)){
            module.onUninstalled(this.player);
            this.currentData.getCurrentModules().remove(module);
            this.currentData.getSlotToModuleMap().remove(this.getSlotIndex());
        }
    }

    private ActiveRarmorModule getActiveModule(){
        Integer i = this.currentData.getSlotToModuleMap().get(this.getSlotIndex());
        if(i != null){
            return this.currentData.getCurrentModules().get(i);
        }
        return null;
    }

    @Override
    public boolean isItemValid(ItemStack stack){
        return stack.getItem() instanceof IRarmorModuleItem && ((IRarmorModuleItem)stack.getItem()).canInstall(this.player, this);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player){
        ItemStack stack = this.getStack();
        ActiveRarmorModule module = this.getActiveModule();
        return stack == null || module == null || !(stack.getItem() instanceof IRarmorModuleItem) || ((IRarmorModuleItem)stack.getItem()).canUninstall(player, this, module);
    }
}
