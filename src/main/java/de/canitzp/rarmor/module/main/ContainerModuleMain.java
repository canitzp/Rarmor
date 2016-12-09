/*
 * This file ("ContainerModuleMain.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.main;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.compat.Compat;
import de.canitzp.rarmor.inventory.ContainerRarmor;
import de.canitzp.rarmor.inventory.slot.SlotModule;
import de.canitzp.rarmor.api.module.IRarmorModuleItem;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ContainerModuleMain extends RarmorModuleContainer {

    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

    private final EntityPlayer player;

    public ContainerModuleMain(EntityPlayer player, Container actualContainer, ActiveRarmorModule module){
        super(actualContainer, module);
        this.player = player;
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<Slot>();

        ActiveModuleMain module = (ActiveModuleMain)this.module;
        slots.add(new Slot(module.inventory, 0, 179, 8));
        slots.add(new Slot(module.inventory, 1, 179, 124));

        for(int i = 0; i < ActiveModuleMain.MODULE_SLOT_AMOUNT; i++){
            slots.add(new SlotModule(this.currentData.getModuleStacks(), this.player, this.currentData, (ContainerRarmor)this.actualContainer, i, 8, 7+i*22));
        }

        for(int i = 0; i < 4; i++){
            final EntityEquipmentSlot slot = VALID_EQUIPMENT_SLOTS[i];
            slots.add(new Slot(ContainerModuleMain.this.player.inventory, 36+(3-i), 49, 16+i*18){
                @Override
                public int getSlotStackLimit(){
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack stack){
                    return slot != EntityEquipmentSlot.CHEST && stack != null && stack.getItem().isValidArmor(stack, slot, ContainerModuleMain.this.player);
                }

                @Override
                @SideOnly(Side.CLIENT)
                public String getSlotTexture(){
                    return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
                }

                @Override
                public ItemStack decrStackSize(int amount){
                    return slot == EntityEquipmentSlot.CHEST ? null : super.decrStackSize(amount);
                }

                @Override
                public void putStack(@Nullable ItemStack stack){
                    if(slot != EntityEquipmentSlot.CHEST){
                        super.putStack(stack);
                    }
                }

                @Override
                public boolean canTakeStack(EntityPlayer playerIn){
                    return slot != EntityEquipmentSlot.CHEST;
                }
            });
        }

        slots.add(new Slot(ContainerModuleMain.this.player.inventory, 40, 49, 94){
            @Override
            public boolean isItemValid(ItemStack stack){
                return super.isItemValid(stack);
            }

            @Override
            @SideOnly(Side.CLIENT)
            public String getSlotTexture(){
                return "minecraft:items/empty_armor_slot_shield";
            }
        });

        return slots;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        int inventoryStart = 5+5; //because of armor and offhand slots
        int inventoryEnd = inventoryStart+26;
        int hotbarStart = inventoryEnd+1;
        int hotbarEnd = hotbarStart+8;

        Slot theSlot = this.actualContainer.inventorySlots.get(slot);

        if(theSlot != null && theSlot.getHasStack()){
            ItemStack newStack = theSlot.getStack();
            ItemStack currentStack = newStack.copy();
            EntityEquipmentSlot equip = EntityLiving.getSlotForItemStack(newStack);

            if(slot >= inventoryStart){
                //Change things here
                if(newStack.getItem() instanceof IRarmorModuleItem && ((IRarmorModuleItem)newStack.getItem()).canInstall(player, theSlot, newStack, this.currentData)){
                    if(!this.mergeItemStack(newStack, 2, 5, false)){
                        return null;
                    }
                }
                else if(equip.getSlotType() == EntityEquipmentSlot.Type.ARMOR){
                    int i = 8-equip.getIndex();

                    if(!this.mergeItemStack(newStack, i, i+1, false)){
                        return null;
                    }
                }
                else if(newStack.getItem() instanceof IEnergyContainerItem){
                    if(!this.mergeItemStack(newStack, 0, 2, false)){
                        return null;
                    }
                }
                else if(Compat.teslaLoaded && newStack.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, EnumFacing.DOWN)){
                    if(!this.mergeItemStack(newStack, 0, 1, false)){
                        return null;
                    }
                }
                else if(Compat.teslaLoaded && newStack.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN)){
                    if(!this.mergeItemStack(newStack, 1, 2, false)){
                        return null;
                    }
                }
                //Not here anymore
                else if(slot >= inventoryStart && slot <= inventoryEnd){
                    if(!this.mergeItemStack(newStack, hotbarStart, hotbarEnd+1, false)){
                        return null;
                    }
                }
                else if(slot >= inventoryEnd+1 && slot < hotbarEnd+1 && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd+1, false)){
                    return null;
                }
            }
            else if(!this.mergeItemStack(newStack, inventoryStart, hotbarEnd+1, false)){
                return null;
            }

            if(newStack.stackSize <= 0){
                theSlot.putStack(null);
            }
            else{
                theSlot.onSlotChanged();
            }

            if(newStack.stackSize == currentStack.stackSize){
                return null;
            }
            theSlot.onPickupFromSlot(player, newStack);

            return currentStack;
        }

        return null;
    }
}
