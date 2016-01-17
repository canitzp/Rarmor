package de.canitzp.rarmor.items.rfarmor;

import cpw.mods.fml.common.registry.GameRegistry;
import de.canitzp.rarmor.Rarmor;
import net.minecraft.item.Item;

/**
 * @author canitzp
 */
public class ItemModule extends Item {

    public ItemModule(String name){
        setMaxStackSize(1);
        setUnlocalizedName(Rarmor.MODID + "." + name);
        setTextureName(Rarmor.MODID + ":" + name);
        GameRegistry.registerItem(this, name);
    }

}
