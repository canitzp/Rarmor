/*
 * This file ("ItemWrench.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.item;

import net.minecraft.item.ItemStack;

public class ItemWireCutter extends ItemBase{

    public ItemWireCutter(String name){
        super(name);

        this.setMaxDamage(15);
        this.setMaxStackSize(1);
        this.setContainerItem(this);
        this.setNoRepair();
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack){
        ItemStack copy = stack.copy();
        copy.setItemDamage(copy.getItemDamage()+1);
        return copy;
    }
}
