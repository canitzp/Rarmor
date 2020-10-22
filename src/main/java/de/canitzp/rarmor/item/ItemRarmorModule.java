/*
 * This file ("ItemRarmorModule.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import de.canitzp.rarmor.api.module.IRarmorModuleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

public abstract class ItemRarmorModule extends ItemBase implements IRarmorModuleItem{

    public ItemRarmorModule(){
        super(new Properties().maxStackSize(1));
    }

    @Override
    public Rarity getRarity(ItemStack stack){
        return Rarity.RARE;
    }
}
