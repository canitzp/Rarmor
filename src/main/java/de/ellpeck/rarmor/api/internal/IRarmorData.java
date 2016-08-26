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

public interface IRarmorData{

    List<ActiveRarmorModule> getCurrentModules();

    String[] getModulesForSlotsArray();

    IInventory getModuleStacks();

    int getSlotForActiveModule(ActiveRarmorModule module);

    int getSelectedModule();

    void writeToNBT(NBTTagCompound compound, boolean sync);

    void readFromNBT(NBTTagCompound compound, boolean sync);

    void selectModule(int i);

    ItemStack getBoundStack();

    void setBoundStack(ItemStack stack);

    void sendQueuedUpdate(EntityPlayer player);

    void uninstallModule(ActiveRarmorModule module, Entity entity, boolean drop);

    void installModule(ItemStack stack, Entity entity, int slotIndex);

    void tick(World world, Entity entity);

    void queueUpdate();

    void queueUpdate(boolean reloadTabs);

    void queueUpdate(boolean reloadTabs, int moduleIdForConfirmation);

    void queueUpdate(boolean reloadTabs, int moduleIdForConfirmation, boolean override);

    ActiveRarmorModule getInstalledModuleWithId(String moduleId);

    int getTotalTickedTicks();

    int getEnergyStored();

    int getMaxEnergyStored();

    int receiveEnergy(int energy, boolean simulate);

    int extractEnergy(int energy, boolean simulate);

    void setEnergy(int energy);
}
