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

import de.canitzp.rarmor.api.module.IActiveRarmorModule;
import de.canitzp.rarmor.api.module.IRarmorModuleItem;
import de.canitzp.rarmor.mod.data.RarmorData;
import de.canitzp.rarmor.mod.misc.Helper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotModule extends Slot{

    private final EntityPlayer player;

    public SlotModule(IInventory inventory, EntityPlayer player, int index, int xPosition, int yPosition){
        super(inventory, index, xPosition, yPosition);
        this.player = player;
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
        ItemStack stack = this.getStack();
        if(stack == null || stack.stackSize <= 0){
            this.uninstallModule();
        }

        super.putStack(newStack);
        if(newStack != null){
            this.installModule(newStack);
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
            RarmorData data = RarmorData.getDataForChestplate(this.player);
            if(data != null){
                IActiveRarmorModule module = Helper.initiateModuleById(((IRarmorModuleItem)stack.getItem()).getModuleIdentifier());
                if(module != null){
                    module.onInstalled(this.player);
                    data.loadedModules.add(module);
                    data.slotToModulePlaceInListMap.put(this.getSlotIndex(), data.loadedModules.indexOf(module));

                    System.out.println("INSTALLED "+data.loadedModules+" "+data.slotToModulePlaceInListMap);
                }
            }
        }
    }

    private void uninstallModule(){
        RarmorData data = RarmorData.getDataForChestplate(this.player);
        IActiveRarmorModule module = this.getActiveModule(data);
        if(module != null){
            module.onUninstalled(this.player);
            data.loadedModules.remove(module);
            data.slotToModulePlaceInListMap.remove(this.getSlotIndex());

            System.out.println("UNINSTALLED "+data.loadedModules+" "+data.slotToModulePlaceInListMap);
        }
    }

    private IActiveRarmorModule getActiveModule(RarmorData data){
        if(data != null){
            Integer i = data.slotToModulePlaceInListMap.get(this.getSlotIndex());
            if(i != null){
                return data.loadedModules.get(i);
            }
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
        IActiveRarmorModule module = this.getActiveModule(RarmorData.getDataForChestplate(this.player));
        return stack == null || module == null || !(stack.getItem() instanceof IRarmorModuleItem) || ((IRarmorModuleItem)stack.getItem()).canUninstall(player, this, module);
    }
}
