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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ActiveRarmorModule{

    public final IRarmorData data;
    public boolean invalid;

    public ActiveRarmorModule(IRarmorData data){
        this.data = data;
    }

    public abstract String getIdentifier();

    public abstract void readFromNBT(NBTTagCompound compound, boolean sync);

    public abstract void writeToNBT(NBTTagCompound compound, boolean sync);

    public abstract RarmorModuleContainer createContainer(EntityPlayer player, Container container);

    @SideOnly(Side.CLIENT)
    public abstract RarmorModuleGui createGui(GuiContainer gui);

    public abstract void onInstalled(Entity entity);

    public abstract void onUninstalled(Entity entity);

    public abstract boolean hasTab(EntityPlayer player);

    @SideOnly(Side.CLIENT)
    public abstract ItemStack getDisplayIcon();

    @Override
    public final boolean equals(Object o){
        return RarmorAPI.methodHandler.compareModules(this, o);
    }

    public abstract void tick(World world, Entity entity);

    @SideOnly(Side.CLIENT)
    public abstract void renderAdditionalOverlay(Minecraft mc, EntityPlayer player, IRarmorData data, ScaledResolution resolution, int renderX, int renderY, float partialTicks);

    @SideOnly(Side.CLIENT)
    public boolean doesRenderOnOverlay(Minecraft mc, EntityPlayer player, IRarmorData data){
        return true;
    }
}
