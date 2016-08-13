/*
 * This file ("ActiveModuleMain.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.module.main;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.mod.inventory.gui.BasicInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;

public class ActiveModuleMain extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Main";

    public BasicInventory inventory = new BasicInventory("modules", 13);

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        this.inventory.loadSlots(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound){
        this.inventory.saveSlots(compound);
    }

    @Override
    public RarmorModuleContainer createContainer(EntityPlayer player, Container container, IRarmorData currentData){
        return new ContainerModuleMain(player, container, this, currentData);
    }

    @Override
    public RarmorModuleGui createGui(GuiContainer container, IRarmorData currentData){
        return new GuiModuleMain(container, this, currentData);
    }

    @Override
    public void onInstalled(EntityPlayer player){
        //Called with null player for this module
    }

    @Override
    public void onUninstalled(EntityPlayer player){
        //Not called for this module
    }

    @Override
    public boolean hasTab(EntityPlayer player){
        return true;
    }
}
