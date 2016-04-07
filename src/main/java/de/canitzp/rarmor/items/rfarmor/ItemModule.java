package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.Rarmor;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class ItemModule extends Item {

    public static Map<String, ItemModule> modules = new HashMap<>();

    public ItemModule(String name) {
        setMaxStackSize(1);
        modules.put(name, this);
        Rarmor.registerItem(this, name);
    }

}
