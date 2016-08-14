/*
 * This file ("ActiveModuleStorage.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.storage;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.inventory.gui.BasicInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ActiveModuleStorage extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Storage";

    public final BasicInventory inventory = new BasicInventory("storage", 46);

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean sync){
        this.inventory.loadSlots(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, boolean sync){
        this.inventory.saveSlots(compound);
    }

    @Override
    public RarmorModuleContainer createContainer(EntityPlayer player, Container container, IRarmorData currentData){
        return new ContainerModuleStorage(player, container, this, currentData);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RarmorModuleGui createGui(GuiContainer gui, IRarmorData currentData){
        return new GuiModuleStorage(gui, this, currentData);
    }

    @Override
    public void onInstalled(EntityPlayer player){

    }

    @Override
    public void onUninstalled(EntityPlayer player){
        for(int i = 0; i < this.inventory.getSizeInventory(); i++){
            ItemStack stack = this.inventory.getStackInSlot(i);
            if(stack != null){
                player.dropItem(stack.copy(), false, false);
                this.inventory.setInventorySlotContents(i, null);
            }
        }
    }

    @Override
    public boolean hasTab(EntityPlayer player){
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIcon(){
        return new ItemStack(Blocks.CHEST);
    }
}
