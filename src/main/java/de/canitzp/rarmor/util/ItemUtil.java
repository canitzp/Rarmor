package de.canitzp.rarmor.util;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class ItemUtil {

    public static Item getItemFromName(String name){
        ResourceLocation loc = new ResourceLocation(name);
        if (Item.itemRegistry.containsKey(loc)) {
            return Item.itemRegistry.getObject(loc);
        }
        else {
            try {
                return Item.itemRegistry.getObjectById(Integer.parseInt(name));
            }
            catch (NumberFormatException numberformatexception) {
                return null;
            }
        }
    }

}
