package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.Rarmor;
import net.minecraft.item.Item;

/**
 * @author canitzp
 */
public class ItemModule extends Item {

    public ItemModule(String name){
        setMaxStackSize(1);
        Rarmor.registerItem(this, name);
    }

}
