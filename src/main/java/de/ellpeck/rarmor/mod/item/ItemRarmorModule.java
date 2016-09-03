/*
 * This file ("ItemRarmorModule.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.item;

import de.ellpeck.rarmor.api.module.IRarmorModuleItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public abstract class ItemRarmorModule extends ItemBase implements IRarmorModuleItem{

    public ItemRarmorModule(String name){
        super(name);
        this.setMaxStackSize(1);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.RARE;
    }
}
