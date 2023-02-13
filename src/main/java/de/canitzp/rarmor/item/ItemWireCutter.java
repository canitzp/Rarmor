/*
 * This file ("ItemWireCutter.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import net.minecraft.world.item.ItemStack;

public class ItemWireCutter extends ItemBase{

    public ItemWireCutter(){
        super(new Properties().stacksTo(1).setNoRepair().durability(127));
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack){
        ItemStack copy = stack.copy();
        copy.setDamageValue(copy.getDamageValue()+1);
        return copy;
    }
}
