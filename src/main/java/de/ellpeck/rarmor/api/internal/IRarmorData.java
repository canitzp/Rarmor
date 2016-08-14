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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IRarmorData{

    List<ActiveRarmorModule> getCurrentModules();

    Map<Integer, String> getSlotToModuleMap();

    int getSelectedModule();

    void writeToNBT(NBTTagCompound compound, boolean sync);

    void readFromNBT(NBTTagCompound compound, boolean sync);

    void selectModule(int i);

    UUID getBoundStackId();

    void sendQueuedUpdate(EntityPlayer player);

    void uninstallModule(ActiveRarmorModule module, EntityPlayer player, int slotIndex);

    void installModule(ItemStack stack, EntityPlayer player, int slotIndex);

    void tick(World world);

    void queueUpdate();

    void queueUpdate(boolean reloadTabs);

    void queueUpdate(boolean reloadTabs, int moduleIdForConfirmation);

    void queueUpdate(boolean reloadTabs, int moduleIdForConfirmation, boolean override);
}
