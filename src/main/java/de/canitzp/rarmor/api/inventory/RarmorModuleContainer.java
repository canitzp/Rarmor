/*
 * This file ("IRarmorModuleContainer.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.api.inventory;

import de.canitzp.rarmor.api.module.IActiveRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

public class RarmorModuleContainer{

    public final IActiveRarmorModule module;

    public RarmorModuleContainer(IActiveRarmorModule module){
        this.module = module;
    }

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
}
