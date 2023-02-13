/*
 * This file ("CreativeTab.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.misc;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.item.RarmorItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTab extends CreativeModeTab {

    public static final CreativeTab INSTANCE = new CreativeTab();

    public CreativeTab(){
        super(RarmorAPI.MOD_ID);
    }

    @Override
    public ItemStack makeIcon(){
        return new ItemStack(RarmorItemRegistry.itemRarmorChest.get());
    }
}
