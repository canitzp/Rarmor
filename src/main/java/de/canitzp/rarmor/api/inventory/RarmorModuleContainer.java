/*
 * This file ("RarmorModuleContainer.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.api.inventory;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;


/**
 * A basic Container class for a Rarmor module that should have a tab that opens a GUI and Container
 * <p>
 * This is similar to Minecraft's Container as it overrides some of its methods, however, the actual container
 * this is contained inside of can be accessed.
 */
public class RarmorModuleContainer{

    private static final EquipmentSlotType[] VALID_EQUIPMENT_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
    private static final String[] ARMOR_SLOT_TEXTURES = new String[]{"item/empty_armor_slot_boots", "item/empty_armor_slot_leggings", "item/empty_armor_slot_chestplate", "item/empty_armor_slot_helmet"};

    public final IRarmorData currentData;
    public final Container actualContainer;
    public final ActiveRarmorModule module;

    public RarmorModuleContainer(Container container, ActiveRarmorModule module){
        this.module = module;
        this.actualContainer = container;
        this.currentData = this.module.data;
    }

    /**
     * Gets all of the slots that should be displayed in this Container.
     * @return a list of all of the slots
     */
    public List<Slot> getSlots(){
        return Collections.emptyList();
    }

    public void detectAndSendChanges(){

    }

    public ItemStack transferStackInSlot(PlayerEntity player, int index){
        return ItemStack.EMPTY;
    }

    public void onContainerClosed(PlayerEntity player){

    }

    public void onCraftMatrixChanged(IInventory inventory){

    }

    @OnlyIn(Dist.CLIENT)
    public void updateProgressBar(int id, int data){

    }

    public void addListener(IContainerListener listener){

    }

    @OnlyIn(Dist.CLIENT)
    public void removeListener(IContainerListener listener){

    }

    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, PlayerEntity player){
        return ItemStack.EMPTY;
    }

    /**
     * This is a helper to merge two stacks inside of transferStackInSlot, as the one in Container is protected
     * and not accessible. This basically does the same thing.
     *
     * @param stack            The itemstack to merge into the slots
     * @param startIndexIncl   The index this should start at, included
     * @param endIndexExcl     The index this should end at, excluded
     * @param reverseDirection If this should happen in the opposite direction
     * @return If this worked or not
     */
    public boolean mergeItemStack(ItemStack stack, int startIndexIncl, int endIndexExcl, boolean reverseDirection){
        return RarmorAPI.methodHandler.mergeItemStack(this.actualContainer, stack, startIndexIncl, endIndexExcl, reverseDirection);
    }

    public void addArmorSlotsAt(PlayerEntity player, List<Slot> slots, int x, int y){
        for(int i = 0; i < 4; i++){
            final EquipmentSlotType slot = VALID_EQUIPMENT_SLOTS[i];
            slots.add(new Slot(player.inventory, 36+(3-i), x, y+i*18){
                @Override
                public int getSlotStackLimit(){
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack stack){
                    return slot != EquipmentSlotType.CHEST && !stack.isEmpty() && stack.canEquip(slot, player);
                }

                @Override
                @OnlyIn(Dist.CLIENT)
                public String getSlotTexture(){
                    return ARMOR_SLOT_TEXTURES[slot.getIndex()];
                }

                @Override
                public ItemStack decrStackSize(int amount){
                    return slot == EquipmentSlotType.CHEST ? null : super.decrStackSize(amount);
                }

                @Override
                public void putStack(@Nullable ItemStack stack){
                    if(slot != EquipmentSlotType.CHEST){
                        super.putStack(stack);
                    }
                }

                @Override
                public boolean canTakeStack(PlayerEntity playerIn){
                    return slot != EquipmentSlotType.CHEST;
                }
            });
        }
    }

    public void addSecondHandSlot(PlayerEntity player, List<Slot> slots, int x, int y){
        slots.add(new Slot(player.inventory, 40, x, y){
            @Override
            public boolean isItemValid(ItemStack stack){
                return super.isItemValid(stack);
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public String getSlotTexture(){
                return "minecraft:items/empty_armor_slot_shield";
            }
        });
    }

}
