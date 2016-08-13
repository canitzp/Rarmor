/*
 * This file ("ItemRarmorModule.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.item;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.module.IRarmorModuleItem;
import de.canitzp.rarmor.mod.misc.CreativeTab;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class ItemRarmorModule extends Item implements IRarmorModuleItem{

    public ItemRarmorModule(String name){
        this.setRegistryName(RarmorAPI.MOD_ID, name);
        GameRegistry.register(this);

        this.setUnlocalizedName(RarmorAPI.MOD_ID+"."+name);
        this.setCreativeTab(CreativeTab.INSTANCE);
    }
}
