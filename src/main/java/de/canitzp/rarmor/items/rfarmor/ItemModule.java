package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.Rarmor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author canitzp
 */
public class ItemModule extends Item {

    public ItemModule(String name){
        setMaxStackSize(1);
        setUnlocalizedName(Rarmor.MODID + "." + name);
        //setTextureName(Rarmor.MODID + ":" + name);
        Rarmor.proxy.addRenderer(new ItemStack(this), name);
        GameRegistry.registerItem(this, name);
    }

}
