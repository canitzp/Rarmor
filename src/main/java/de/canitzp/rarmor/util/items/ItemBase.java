/*
 * This file 'ItemBase.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.util.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author canitzp
 */
public class ItemBase extends Item{

    public ItemBase(String modid, String name, CreativeTabs tabs){
        setUnlocalizedName(modid + ":" + name);
        setCreativeTab(tabs);
        setRegistryName(name);
        GameRegistry.register(this);
    }

}
