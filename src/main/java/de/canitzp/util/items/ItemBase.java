package de.canitzp.util.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author canitzp
 */
public class ItemBase extends Item {

    public ItemBase(String modid, String name, CreativeTabs tabs){
        setUnlocalizedName(modid + "." + name);
        setCreativeTab(tabs);
        setRegistryName(modid + "." + name);
        GameRegistry.registerItem(this, name);
    }

}
