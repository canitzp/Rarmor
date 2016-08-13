/*
 * This file ("IRarmorModuleItem.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.api.module;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public interface IRarmorModuleItem{

    String getModuleIdentifier();

    boolean canInstall(EntityPlayer player, Slot slot);

    boolean canUninstall(EntityPlayer player, Slot slot, IActiveRarmorModule module);
}
