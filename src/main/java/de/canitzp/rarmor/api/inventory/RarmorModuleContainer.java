/*
 * This file ("RarmorModuleContainer.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.api.inventory;

import com.mojang.datafixers.util.Pair;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
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

    private static final EquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET};

    public final IRarmorData currentData;
    public final AbstractContainerMenu actualContainer;
    public final ActiveRarmorModule module;

    public RarmorModuleContainer(AbstractContainerMenu container, ActiveRarmorModule module){
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

    public ItemStack transferStackInSlot(Player player, int index){
        return ItemStack.EMPTY;
    }

    public void onContainerClosed(Player player){

    }

    public void onCraftMatrixChanged(Container inventory){

    }

    public void addListener(ContainerListener listener){

    }

    @OnlyIn(Dist.CLIENT)
    public void removeListener(ContainerListener listener){

    }

    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, Player player){
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

    public void addArmorSlotsAt(Player player, List<Slot> slots, int x, int y){
        for(int i = 0; i < 4; i++){
            final EquipmentSlot slot = VALID_EQUIPMENT_SLOTS[i];
            slots.add(new Slot(player.getInventory(), 36+(3-i), x, y+i*18){

                @Override
                public int getMaxStackSize() {
                    return 1;
                }
    
                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon(){
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, ARMOR_SLOT_TEXTURES[slot.getIndex()]);
                }

                @Override
                public void set(@Nullable ItemStack stack){
                    if(slot != EquipmentSlot.CHEST){
                        super.set(stack);
                    }
                }

                @Override
                public boolean allowModification(Player player) {
                    return slot != EquipmentSlot.CHEST && super.allowModification(player);
                }
            });
        }
    }

    public void addSecondHandSlot(Player player, List<Slot> slots, int x, int y){
        slots.add(new Slot(player.getInventory(), 40, x, y){
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon(){
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
    }

}
