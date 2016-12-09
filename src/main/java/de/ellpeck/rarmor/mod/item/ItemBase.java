/*
 * This file ("ItemBase.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.item;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.mod.misc.CreativeTab;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBase extends Item{

    public ItemBase(String name){
        this.setRegistryName(RarmorAPI.MOD_ID, name);
        GameRegistry.register(this);

        this.setUnlocalizedName(RarmorAPI.MOD_ID+"."+name);
        this.setCreativeTab(CreativeTab.INSTANCE);
    }
}
