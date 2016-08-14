/*
 * This file ("IRarmorData.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.api.internal;

import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IRarmorData{

    List<ActiveRarmorModule> getCurrentModules();

    Map<Integer, Integer> getSlotToModuleMap();

    int getSelectedModule();

    void writeToNBT(NBTTagCompound compound, boolean sync);

    void readFromNBT(NBTTagCompound compound, boolean sync);

    void selectModule(int i);

    UUID getBoundStackId();

    void sendUpdate(EntityPlayer player, boolean reloadTabs, int moduleIdForConfirmation);
}
