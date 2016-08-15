/*
 * This file ("ActiveRarmorModule.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.api.module;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ActiveRarmorModule{

    public final IRarmorData data;

    public ActiveRarmorModule(IRarmorData data){
        this.data = data;
    }

    public abstract String getIdentifier();

    public abstract void readFromNBT(NBTTagCompound compound, boolean sync);

    public abstract void writeToNBT(NBTTagCompound compound, boolean sync);

    public abstract RarmorModuleContainer createContainer(EntityPlayer player, Container container);

    @SideOnly(Side.CLIENT)
    public abstract RarmorModuleGui createGui(GuiContainer gui);

    public abstract void onInstalled(EntityPlayer player);

    public abstract void onUninstalled(EntityPlayer player);

    public abstract boolean hasTab(EntityPlayer player);

    @SideOnly(Side.CLIENT)
    public abstract ItemStack getTabIcon();

    @Override
    public final boolean equals(Object o){
        return RarmorAPI.methodHandler.compareModules(this, o);
    }

    public void tick(World world){

    }
}
