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
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Map;

public interface IRarmorData{

    List<ActiveRarmorModule> getCurrentModules();

    Map<Integer, Integer> getSlotToModuleMap();

    int getSelectedModule();

    void writeToNBT(NBTTagCompound compound);

    void readFromNBT(NBTTagCompound compound);

    void selectModule(int i);
}
