/*
 * This file ("IRarmorData.java") is part of the Rarmor mod for Minecraft.
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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

/**
 * An interface to accesss the Rarmor Data from the API.
 * <p>
 * This is not supposed to be implemented.
 */
public interface IRarmorData{

    /**
     * Gets the list of currently loaded and actively ticking modules
     *
     * @return the list of modules
     */
    List<ActiveRarmorModule> getCurrentModules();

    /**
     * Gets a map of strings that corresponds to the id of the module loaded in the current slot.
     * This shouldn't ever really be useful to anyone who is making an add-on though.
     *
     * @return The map of strings
     */
    Map<String, Integer> getModuleIdsForSlots();

    /**
     * Returns the inventory containing all of the modules that are currently inside the Rarmor
     *
     * @return The inventory
     */
    IInventory getModuleStacks();

    /**
     * Gets the slot a module's item is currently in.
     *
     * @param module The module
     * @return The slot the module is in, or -1 if none.
     */
    int getSlotForActiveModule(ActiveRarmorModule module);

    /**
     * Gets the identifier of the module for an item that is in the specified slot
     *
     * @param slot The slot
     * @return The module that is in the slot
     */
    String getActiveModuleForSlot(int slot);

    /**
     * Gets the place in the list of current modules that the currently selected module is in
     *
     * @return The place in the list
     */
    int getSelectedModule();

    /**
     * Writes this Data to NBT.
     *
     * @param compound The compound of NBT to write to
     * @param sync     If this is for syncing or for saving to disc
     */
    void writeToNBT(NBTTagCompound compound, boolean sync);

    /**
     * Reads this Data from NBT.
     *
     * @param compound The compound of NBT to read from
     * @param sync     If this is for syncing or for saving to disc
     */
    void readFromNBT(NBTTagCompound compound, boolean sync);

    /**
     * Selects a module.
     *
     * @param i The place in the list of current modules of the module that should be selected
     */
    void selectModule(int i);

    /**
     * Gets the stack that this data is bound to
     *
     * @return The stack
     */
    ItemStack getBoundStack();

    /**
     * Sets the stack that this data is bound to
     *
     * @param stack The stack
     */
    void setBoundStack(ItemStack stack);

    /**
     * Sends an update that is queued.
     * <p>
     * Do not call this, it is for internal use only.
     * An update will be sent every tick as long as the Rarmor is in the player's inventory.
     * To send updates yourself, use queueUpdate below.
     *
     * @param player The player to send the update to
     */
    void sendQueuedUpdate(EntityPlayer player);

    /**
     * Uninstalls a module.
     *
     * @param module The module to uninstall
     * @param entity The entity that is trying to uninstall the module
     * @param drop   If the item should be removed from the slot and dropped
     */
    void uninstallModule(ActiveRarmorModule module, Entity entity, boolean drop);

    /**
     * Installs a module.
     *
     * @param stack     The stack the module's item is in
     * @param entity    The entity that is trying to install the module
     * @param slotIndex The slot the module should be put in
     */
    void installModule(ItemStack stack, Entity entity, int slotIndex);

    /**
     * Ticks this data.
     * For internal use only.
     *
     * @param world          The world
     * @param entity         The entity
     * @param isWearingHat   If the entity is currently wearing the Rarmor hat
     * @param isWearingChest If the entity is currently wearing the Rarmor chestpalte
     * @param isWearingPants If the entity is currently wearing the Rarmor pants
     * @param isWearingShoes If the entity is currently wearing the Rarmor shoes
     */
    void tick(World world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes);

    /**
     * Queues an update.
     */
    void queueUpdate();

    /**
     * Queues an update.
     *
     * @param reloadTabs If the tabs on the side should be reinitiated or not
     */
    void queueUpdate(boolean reloadTabs);

    /**
     * Queues an update.
     * This version should never really be needed and is for internal use only.
     *
     * @param reloadTabs              If the tabs on the side should be reinitiated or not
     * @param moduleIdForConfirmation The module's id that is sent to and then back from the client to confirm
     *                                that the packet was received. Use a negative number if this shouldn't happen.
     */
    void queueUpdate(boolean reloadTabs, int moduleIdForConfirmation);

    /**
     * Queues an update.
     * This version should never really be needed and is for internal use only.
     *
     * @param reloadTabs              If the tabs on the side should be reinitiated or not
     * @param moduleIdForConfirmation The module's id that is sent to and then back from the client to confirm
     *                                that the packet was received. Use a negative number if this shouldn't happen.
     * @param override                If this update should override a currently already queued one.
     *                                This is useful if reloadTabs or moduleIdForConfirmation is important.
     */
    void queueUpdate(boolean reloadTabs, int moduleIdForConfirmation, boolean override);

    /**
     * Checks if a module with a given id is installed
     *
     * @param moduleId The id
     * @return The module if it is installed, null otherwise
     */
    ActiveRarmorModule getInstalledModuleWithId(String moduleId);

    /**
     * Gets the amount of ticks that this Rarmor data has existed for.
     *
     * @return The ticks
     */
    int getTotalTickedTicks();

    /**
     * Gets the amount of RF or Tesla that the Rarmor this data is bound has stored
     *
     * @return The energy
     */
    int getEnergyStored();

    /**
     * Gets the max amount of RF or Tesla that the Rarmor this data is bound can store
     *
     * @return The max energy
     */
    int getMaxEnergyStored();

    /**
     * Tries to make the Rarmor this data is bound to receive a certain amount of energy
     *
     * @param energy   The amount of energy to receive
     * @param simulate If this operation should actually happen or just be simulated
     * @return The amount that could be received
     */
    int receiveEnergy(int energy, boolean simulate);

    /**
     * Tries to make the Rarmor this data is bound to extract a certain amount of energy
     *
     * @param energy   The amount of energy to extract
     * @param simulate If this operation should actually happen or just be simulated
     * @return The amount that could be extracted
     */
    int extractEnergy(int energy, boolean simulate);

    /**
     * Sets the amount of energy that this Rarmor has currently stored
     *
     * @param energy The amount of energy this Rarmor should have
     */
    void setEnergy(int energy);

    /**
     * Gets if this data should be saved to memory next time a world save happens
     *
     * @return If the data is dirty
     */
    boolean getDirty();

    /**
     * Sets if this data should be saved to memory next time a world save happens
     *
     * @param yes If this should happen or not
     */
    void setDirty(boolean yes);

    /**
     * Sets if this data should be saved to memory next time a world save happens.
     * This is a convenience method as it will always set the value to true.
     */
    void setDirty();
}
