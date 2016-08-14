/*
 * This file ("ItemModuleStorage.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.module.storage;

import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.mod.item.ItemRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ItemModuleStorage extends ItemRarmorModule{

    public ItemModuleStorage(String name){
        super(name);
    }

    @Override
    public String getModuleIdentifier(){
        return ActiveModuleStorage.IDENTIFIER;
    }

    @Override
    public boolean canInstall(EntityPlayer player, Slot slot){
        return true;
    }

    @Override
    public boolean canUninstall(EntityPlayer player, Slot slot, ActiveRarmorModule module){
        return true;
    }
}
