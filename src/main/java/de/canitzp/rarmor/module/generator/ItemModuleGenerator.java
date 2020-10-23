/*
 * This file ("ItemModuleGenerator.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.generator;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.item.ItemRarmorModule;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ItemModuleGenerator extends ItemRarmorModule{
    
    @Override
    public String[] getModuleIdentifiers(ItemStack stack){
        return new String[]{ActiveModuleGenerator.IDENTIFIER};
    }

    @Override
    public boolean canInstall(PlayerEntity player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }

    @Override
    public boolean canUninstall(PlayerEntity player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }
}
