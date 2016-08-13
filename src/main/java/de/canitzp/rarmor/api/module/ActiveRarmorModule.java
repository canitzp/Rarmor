/*
 * This file ("IActiveRarmorModule.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.api.module;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ActiveRarmorModule{

    public abstract String getIdentifier();

    public abstract void readFromNBT(NBTTagCompound compound);

    public abstract void writeToNBT(NBTTagCompound compound);

    public abstract RarmorModuleContainer createContainer(EntityPlayer player, Container container, IRarmorData currentData);

    @SideOnly(Side.CLIENT)
    public abstract RarmorModuleGui createGui(GuiContainer gui, IRarmorData currentData);

    public abstract void onInstalled(EntityPlayer player);

    public abstract void onUninstalled(EntityPlayer player);

    public abstract boolean hasTab(EntityPlayer player);

    @Override
    public boolean equals(Object obj){
        return obj instanceof ActiveRarmorModule && this.getIdentifier().equals(((ActiveRarmorModule)obj).getIdentifier());
    }
}
