/*
 * This file ("ItemModuleFurnace.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.furnace;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.item.ItemRarmorModule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ItemModuleFurnace extends ItemRarmorModule{
    
    @Override
    public String[] getModuleIdentifiers(ItemStack stack){
        return new String[]{ActiveModuleFurnace.IDENTIFIER};
    }

    @Override
    public boolean canInstall(Player player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }

    @Override
    public boolean canUninstall(Player player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }
}
