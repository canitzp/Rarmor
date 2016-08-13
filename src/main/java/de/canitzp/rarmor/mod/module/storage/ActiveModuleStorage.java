/*
 * This file ("ActiveModuleStorage.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.module.storage;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;

public class ActiveModuleStorage extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Storage";

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){

    }

    @Override
    public void writeToNBT(NBTTagCompound compound){

    }

    @Override
    public RarmorModuleContainer createContainer(EntityPlayer player, Container container, IRarmorData currentData){
        return null;
    }

    @Override
    public RarmorModuleGui createGui(GuiContainer gui, IRarmorData currentData){
        return null;
    }

    @Override
    public void onInstalled(EntityPlayer player){
        System.out.println("I BE INSTALLED!!");
    }

    @Override
    public void onUninstalled(EntityPlayer player){
        System.out.println("I BE UNINSTALLED!!");
    }

    @Override
    public boolean hasTab(EntityPlayer player){
        return true;
    }
}
