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

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IActiveRarmorModule{

    void readFromNBT(NBTTagCompound compound);

    void writeToNBT(NBTTagCompound compound);

    String getUniqueIdentifier();

    Container createContainer();

    @SideOnly(Side.CLIENT)
    GuiScreen createGui();

    void onInstalled();

    void onUninstalled();
}
