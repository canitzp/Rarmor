/*
 * This file ("ContainerModuleMain.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.main;

import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.inventory.ContainerRarmor;
import de.ellpeck.rarmor.mod.inventory.slot.SlotModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ContainerModuleMain extends RarmorModuleContainer{

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
}
