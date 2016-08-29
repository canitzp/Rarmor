/*
 * This file ("CreativeTab.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.misc;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.mod.item.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTab extends CreativeTabs{

    public static final CreativeTab INSTANCE = new CreativeTab();

    public CreativeTab(){
        super(RarmorAPI.MOD_ID);
    }

    @Override
    public Item getTabIconItem(){
        return ItemRegistry.itemRarmorChest;
    }
}
