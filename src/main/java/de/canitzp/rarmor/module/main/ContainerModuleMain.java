/*
 * This file ("ContainerModuleMain.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.main;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.inventory.ContainerRarmor;
import de.canitzp.rarmor.inventory.slot.SlotModule;
import de.canitzp.rarmor.api.module.IRarmorModuleItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ContainerModuleMain extends RarmorModuleContainer {

    private final Player player;

    public ContainerModuleMain(Player player, AbstractContainerMenu actualContainer, ActiveRarmorModule module){
        super(actualContainer, module);
        this.player = player;
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<>();

        ActiveModuleMain module = (ActiveModuleMain)this.module;
        slots.add(new Slot(module.inventory, 0, 179, 8));
        slots.add(new Slot(module.inventory, 1, 179, 124));

        for(int i = 0; i < ActiveModuleMain.MODULE_SLOT_AMOUNT; i++){
            slots.add(new SlotModule(this.currentData.getModuleStacks(), this.player, this.currentData, (ContainerRarmor)this.actualContainer, i, 8, 7+i*22));
        }

        this.addArmorSlotsAt(ContainerModuleMain.this.player, slots, 49, 16);
        this.addSecondHandSlot(ContainerModuleMain.this.player, slots, 49, 94);

        return slots;
    }

    @Override
    public ItemStack transferStackInSlot(Player player, int slot){
        int inventoryStart = 5+5; //because of armor and offhand slots
        int inventoryEnd = inventoryStart+26;
        int hotbarStart = inventoryEnd+1;
        int hotbarEnd = hotbarStart+8;

        Slot theSlot = this.actualContainer.slots.get(slot);

        if(theSlot != null && theSlot.hasItem()){
            ItemStack newStack = theSlot.getItem();
            ItemStack currentStack = newStack.copy();
            EquipmentSlot equip = Mob.getEquipmentSlotForItem(newStack);

            if(slot >= inventoryStart){
                //Change things here
                if(newStack.getItem() instanceof IRarmorModuleItem && ((IRarmorModuleItem)newStack.getItem()).canInstall(player, theSlot, newStack, this.currentData)){
                    if(!this.mergeItemStack(newStack, 2, 5, false)){
                        return ItemStack.EMPTY;
                    }
                }
                else if(equip.getType() == EquipmentSlot.Type.ARMOR){
                    int i = 8-equip.getIndex();

                    if(!this.mergeItemStack(newStack, i, i+1, false)){
                        return ItemStack.EMPTY;
                    }
                }
                /* To not annoy anyone who tries to shit a battery into the inventory or hotbar
                else if(newStack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN)){
                    if(!this.mergeItemStack(newStack, 0, 1, false)){
                        return ItemStack.EMPTY;
                    }
                }
                */
                //Not here anymore
                else if(slot >= inventoryStart && slot <= inventoryEnd){
                    if(!this.mergeItemStack(newStack, hotbarStart, hotbarEnd+1, false)){
                        return ItemStack.EMPTY;
                    }
                }
                else if(slot >= inventoryEnd+1 && slot < hotbarEnd+1 && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd+1, false)){
                    return ItemStack.EMPTY;
                }
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
