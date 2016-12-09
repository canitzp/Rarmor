/*
 * This file ("IRarmorModuleItem.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.api.module;

import de.canitzp.rarmor.api.internal.IRarmorData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * The basic class for an item that contains a Rarmor module.
 * This has to be implemented for module items, otherwise they won't be able to be installed.
 */
public interface IRarmorModuleItem{

    /**
     * Gets the identifiers of the modules that this item is representing
     *
     * @param stack The stack
     * @return The identifiers
     */
    String[] getModuleIdentifiers(ItemStack stack);

    /**
     * Gets if this module can be installed into the given slot
     *
     * @param player      The player that is trying to install this
     * @param slot        The slot it is trying to be installed into
     * @param stack       The stack
     * @param currentData The current Rarmor Data
     * @return If this can be installed
     */
    boolean canInstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData);

    /**
     * Gets if this module can be uninstalled from the given slot
     * Please don't just return false on this, because then the module will have to stay in the slot forever :(
     *
     * @param player      The player that is trying to uninstall this
     * @param slot        The slot it is trying to be uninstalled from
     * @param stack       The stack
     * @param currentData The current Rarmor Data
     * @return If this can be uninstalled
     */
    boolean canUninstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData);
}
