/*
 * This file ("IMethodHandler.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.api.internal;

import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

/**
 * The internal method handler.
 * Use the RarmorAPI.methodHandler variable to access the methods below.
 * <p>
 * This is not supposed to be implemented.
 */
public interface IMethodHandler{

    /**
     * Checks if the given entity has a rarmor item in the given slot.
     *
     * @param entity The entity
     * @param slot   The slot
     * @return The rarmor item if the entity has it in the slot, otherwise null
     */
    ItemStack getHasRarmorInSlot(Entity entity, EntityEquipmentSlot slot);

    /**
     * Gets the Rarmor Data for the chestplate a player is wearing
     *
     * @param player         The player
     * @param createIfAbsent If new data should be created if there is none available
     * @return The data for the chestplate the player is wearing, null if not available
     */
    IRarmorData getDataForChestplate(EntityPlayer player, boolean createIfAbsent);

    /**
     * Gets the Rarmor Data for a given stack in the given world
     *
     * @param world          The world
     * @param stack          The ItemStack
     * @param createIfAbsent If new data should be created if there is none available
     * @return The data for the given stack, null if not available
     */
    IRarmorData getDataForStack(World world, ItemStack stack, boolean createIfAbsent);

    /**
     * Internal method, compares two rarmor modules
     *
     * @param module One module
     * @param o      The object to compare to the module
     * @return If the two modules are equal
     */
    boolean compareModules(ActiveRarmorModule module, Object o);

    /**
     * Checks if a given stack has a unique Rarmor ID assigned to it
     *
     * @param stack          The stack
     * @param createIfAbsent If the id should be created if there is none available
     * @return The UUID for the given stack, null if not available
     */
    UUID checkAndSetRarmorId(ItemStack stack, boolean createIfAbsent);

    /**
     * Opens the Rarmor GUI for the Rarmor the given player is currently wearing in their chestplate slot.
     * Call this from the server.
     *
     * @param player                 The player
     * @param moduleId               The place in the list of modules whos GUI to open
     * @param alsoSetData            If the module ID given should be assigned as the currently open one
     * @param sendRarmorDataToClient If the data the Rarmor has should be sent to the client
     *                               (possibly confirmation packet is enabled, so there might be a delay)
     */
    void openRarmor(EntityPlayer player, int moduleId, boolean alsoSetData, boolean sendRarmorDataToClient);

    /**
     * Opens the Rarmor GUI for the Rarmor Minecraft.getMinecraft().thePlayeris currently wearing in their chestplate slot.
     * Call this from the client.
     *
     * @param moduleId               The place in the list of modules whos GUI to open
     * @param alsoSetData            If the module ID given should be assigned as the currently open one
     * @param sendRarmorDataToClient If the data the Rarmor has should be sent to the client
     *                               (possibly confirmation packet is enabled, so there might be a delay)
     */
    @SideOnly(Side.CLIENT)
    void openRarmorFromClient(int moduleId, boolean alsoSetData, boolean sendRarmorDataToClient);

    /**
     * This is a helper to merge two stacks inside of transferStackInSlot, as the one in Container is protected
     * and not accessible inside of RarmorModuleContainer. Which is very annoying.
     * <p>
     * This only works if the container that is currently open is the Rarmor.
     *
     * @param container        The container this is happening in
     * @param stack            The itemstack to merge into the slots
     * @param startIndexIncl   The index this should start at, included
     * @param endIndexExcl     The index this should end at, excluded
     * @param reverseDirection If this should happen in the opposite direction
     * @return If this worked or not
     */
    boolean mergeItemStack(Container container, ItemStack stack, int startIndexIncl, int endIndexExcl, boolean reverseDirection);
}
