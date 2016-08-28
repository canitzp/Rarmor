/*
 * This file ("ItemModuleEnder.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.furnace;

import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.item.ItemRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ItemModuleFurnace extends ItemRarmorModule{

    public ItemModuleFurnace(String name){
        super(name);
    }

    @Override
    public String getModuleIdentifier(ItemStack stack){
        return ActiveModuleFurnace.IDENTIFIER;
    }

    @Override
    public boolean canInstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }

    @Override
    public boolean canUninstall(EntityPlayer player, Slot slot, ItemStack stack, ActiveRarmorModule module, IRarmorData currentData){
        return true;
    }
}
