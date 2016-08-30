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

/**
 * A basic class for a Rarmor module. The way this works is similar to a TileEntity in that every module is a seperate
 * instance of this class.
 * Because of this, extensions of this class need to have a constructor that only takes in an IRarmorData or it will crash.
 * <p>
 * Extend this to make a custom module. The class then has to be registered using the RarmorAPI.registerRarmorModule method.
 */
public abstract class ActiveRarmorModule{

    public final IRarmorData data;
    /**
     * If this module is invalid. Can be set to true in the tick method to avoid concurrent modifications.
     * If so, it will get removed from the list of modules and its item will be dropped.
     */
    public boolean invalid;

    public ActiveRarmorModule(IRarmorData data){
        this.data = data;
    }

    /**
     * Gets the identifier of this module.
     * Has to be the exact same as the one the module is registered with.
     *
     * @return The identifier
     */
    public abstract String getIdentifier();

    /**
     * Reads this module from NBT.
     *
     * @param compound The compound of NBT to read from
     * @param sync     If this is for syncing or for saving to disc
     */
    public abstract void readFromNBT(NBTTagCompound compound, boolean sync);

    /**
     * Writes this module to NBT.
     *
     * @param compound The compound of NBT to write to
     * @param sync     If this is for syncing or for saving to disc
     */
    public abstract void writeToNBT(NBTTagCompound compound, boolean sync);

    /**
     * Creates a new container when opening this' tab.
     * This is only needed when the hasTab method returns true.
     *
     * @param player    The player that is opening the tab
     * @param container The base Rarmor container the tab is opened on
     * @return A new module container
     */
    public abstract RarmorModuleContainer createContainer(EntityPlayer player, Container container);

    /**
     * Creates a new GUI when opening this' tab.
     * This is only needed when the hasTab method returns true.
     *
     * @param gui The base Rarmor GUI the tab is opened on
     * @return A new module container
     */
    @SideOnly(Side.CLIENT)
    public abstract RarmorModuleGui createGui(GuiContainer gui);

    /**
     * Called when this module is installed into a Rarmor
     *
     * @param entity The entity that is installing this
     */
    public abstract void onInstalled(Entity entity);

    /**
     * Called when this module is uninstalled from a Rarmor
     *
     * @param entity The entity that is uninstalling this
     */
    public abstract void onUninstalled(Entity entity);

    /**
     * Gets if this module should have an openable tab on the side of the Rarmor.
     *
     * @param player The player that is trying to have a tab
     * @return If this module has a tab
     */
    public abstract boolean hasTab(EntityPlayer player);

    /**
     * Gets the icon that is displayed in the overlay on the top left of the screen
     * and the tab on the right side of the Rarmor.
     * If neither is enabled, this can return null.
     * <p>
     * Don't just return a new ItemStack here, as this is being called every tick and would produce
     * unnecessary memory usage.
     *
     * @return The Item to be displayed.
     */
    @SideOnly(Side.CLIENT)
    public abstract ItemStack getDisplayIcon();

    @Override
    public final boolean equals(Object o){
        return RarmorAPI.methodHandler.compareModules(this, o);
    }

    /**
     * Ticks this module.
     * This is the equivalent of ITickable's update method.
     *
     * @param world  The world
     * @param entity The entity
     */
    public abstract void tick(World world, Entity entity);

    /**
     * This can be used to render additional information to the overlay displayed on the top left of the screen
     *
     * @param mc The Minecraft instance
     * @param player The player that is wearing the Rarmor
     * @param data The data (same as this' data variable)
     * @param resolution The current resolution
     * @param renderX The x position to render this module at
     * @param renderY The y position to render this module at
     * @param partialTicks The amount of partial ticks
     */
    @SideOnly(Side.CLIENT)
    public abstract void renderAdditionalOverlay(Minecraft mc, EntityPlayer player, IRarmorData data, ScaledResolution resolution, int renderX, int renderY, float partialTicks);

    /**
     * Gets if this module should appear on the overlay on the top left of the screen
     * @param mc The Minecraft instance
     * @param player The player that is wearing the Rarmor
     * @param data The data (same as this' data variable)
     * @return If this should be displayed
     */
    @SideOnly(Side.CLIENT)
    public boolean doesRenderOnOverlay(Minecraft mc, EntityPlayer player, IRarmorData data){
        return true;
    }
}
