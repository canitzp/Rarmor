/*
 * This file ("RarmorModuleContainer.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.api.inventory;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

/**
 * A basic Container class for a Rarmor module that should have a tab that opens a GUI and Container
 * <p>
 * This is similar to Minecraft's Container as it overrides some of its methods, however, the actual container
 * this is contained inside of can be accessed.
 */
public class RarmorModuleContainer{

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

    public ItemStack transferStackInSlot(EntityPlayer player, int index){
        return null;
    }

    public void onContainerClosed(EntityPlayer player){

    }

    public void onCraftMatrixChanged(IInventory inventory){

    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data){

    }

    public void addListener(IContainerListener listener){

    }

    @SideOnly(Side.CLIENT)
    public void removeListener(IContainerListener listener){

    }

    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player){
        return null;
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
}
