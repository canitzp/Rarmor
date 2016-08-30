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
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public interface IMethodHandler{

    ItemStack getHasRarmorInSlot(Entity entity, EntityEquipmentSlot slot);

    IRarmorData getDataForChestplate(EntityPlayer player, boolean createIfAbsent);

    IRarmorData getDataForStack(World world, ItemStack stack, boolean createIfAbsent);

    boolean compareModules(ActiveRarmorModule module, Object o);

    UUID checkAndSetRarmorId(ItemStack stack, boolean createIfAbsent);

    void openRarmor(EntityPlayer player, int moduleId, boolean alsoSetData, boolean sendRarmorDataToClient);

    @SideOnly(Side.CLIENT)
    void openRarmorFromClient(int moduleId, boolean alsoSetData, boolean sendRarmorDataToClient);
}
