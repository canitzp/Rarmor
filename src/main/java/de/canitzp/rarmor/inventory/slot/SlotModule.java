/*
 * This file ("SlotModule.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.inventory.slot;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.api.module.IRarmorModuleItem;
import de.canitzp.rarmor.inventory.ContainerRarmor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SlotModule extends Slot {

    private final IRarmorData currentData;
    private final PlayerEntity player;
    private final ContainerRarmor container;

    public SlotModule(IInventory inventory, PlayerEntity player, IRarmorData currentData, ContainerRarmor container, int index, int xPosition, int yPosition){
        super(inventory, index, xPosition, yPosition);
        this.player = player;
        this.currentData = currentData;
        this.container = container;
    }

    @Nonnull
    @Override
    public ItemStack onTake(@Nonnull PlayerEntity player, @Nonnull ItemStack someStack){
        this.uninstallModule();
        return super.onTake(player, someStack);
    }

    @Override
    public void putStack(@Nonnull ItemStack newStack){
        super.putStack(newStack);

        if(!this.container.isPuttingStacksInSlots){
            if(!newStack.isEmpty()){
                this.installModule(newStack);
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int amount){
        ItemStack toReturn = super.decrStackSize(amount);

        ItemStack stack = this.getStack();
        if(!stack.isEmpty() || stack.getCount() <= 0){
            this.uninstallModule();
        }

        return toReturn;
    }

    private void installModule(ItemStack stack){
        this.currentData.installModule(stack, this.player, this.getSlotIndex());

        if(!this.player.getEntityWorld().isRemote){
            this.currentData.queueUpdate(true, -1, true);
        }
    }

    private void uninstallModule(){
        List<ActiveRarmorModule> modules = this.getActiveModules(this.getSlotIndex(), this.currentData);
        for(ActiveRarmorModule module : modules){
            this.currentData.uninstallModule(module, this.player, false);
        }

        if(!this.player.getEntityWorld().isRemote){
            this.currentData.queueUpdate(true, -1, true);
        }
    }

    private List<ActiveRarmorModule> getActiveModules(int slotIndex, IRarmorData data){
        List<String> identifiers = data.getActiveModulesForSlot(slotIndex);
        List<ActiveRarmorModule> modules = new ArrayList<ActiveRarmorModule>();

        if(!identifiers.isEmpty()){
            for(String identifier : identifiers){
                if(identifier != null){
                    for(ActiveRarmorModule module : data.getCurrentModules()){
                        if(identifier.equals(module.getIdentifier())){
                            modules.add(module);
                        }
                    }
                }
            }

        }
        return modules;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack){
        ItemStack myStack = this.getStack();
        if(!myStack.isEmpty() || myStack.getCount() <= 0){
            if(stack.getItem() instanceof IRarmorModuleItem){
                IRarmorModuleItem item = (IRarmorModuleItem)stack.getItem();
                if(item.canInstall(this.player, this, stack, this.currentData)){
                    String[] ids = item.getModuleIdentifiers(stack);
                    if(ids != null){
                        for(String id : ids){
                            if(this.currentData.getInstalledModuleWithId(id) != null){
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean canTakeStack(@Nonnull PlayerEntity player){
        ItemStack stack = this.getStack();
        return stack.isEmpty() || this.getActiveModules(this.getSlotIndex(), this.currentData).isEmpty() || !(stack.getItem() instanceof IRarmorModuleItem) || ((IRarmorModuleItem)stack.getItem()).canUninstall(player, this, stack, this.currentData);
    }
}
