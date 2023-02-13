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
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SlotModule extends Slot {

    private final IRarmorData currentData;
    private final Player player;
    private final ContainerRarmor container;

    public SlotModule(Container inventory, Player player, IRarmorData currentData, ContainerRarmor container, int index, int xPosition, int yPosition){
        super(inventory, index, xPosition, yPosition);
        this.player = player;
        this.currentData = currentData;
        this.container = container;
    }

    @Override
    public void onTake(@Nonnull Player player, @Nonnull ItemStack someStack){
        this.uninstallModule();
        super.onTake(player, someStack);
    }

    @Override
    public void set(@Nonnull ItemStack newStack){
        super.set(newStack);

        if(!this.container.isPuttingStacksInSlots){
            if(!newStack.isEmpty()){
                this.installModule(newStack);
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack remove(int amount){
        ItemStack toReturn = super.remove(amount);

        ItemStack stack = this.getItem();
        if(!stack.isEmpty() || stack.getCount() <= 0){
            this.uninstallModule();
        }

        return toReturn;
    }

    private void installModule(ItemStack stack){
        this.currentData.installModule(stack, this.player, this.getSlotIndex());

        if(!this.player.getLevel().isClientSide()){
            this.currentData.queueUpdate(true, -1, true);
        }
    }

    private void uninstallModule(){
        List<ActiveRarmorModule> modules = this.getActiveModules(this.getSlotIndex(), this.currentData);
        for(ActiveRarmorModule module : modules){
            this.currentData.uninstallModule(module, this.player, false);
        }

        if(!this.player.getLevel().isClientSide()){
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
    public boolean mayPlace(@Nonnull ItemStack stack){
        ItemStack myStack = this.getItem();
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
                        return super.mayPlace(stack);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean mayPickup(@Nonnull Player player){
        ItemStack stack = this.getItem();
        return stack.isEmpty() || this.getActiveModules(this.getSlotIndex(), this.currentData).isEmpty() || !(stack.getItem() instanceof IRarmorModuleItem) || ((IRarmorModuleItem)stack.getItem()).canUninstall(player, this, stack, this.currentData);
    }
}
