package de.canitzp.rarmor.items;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.util.items.ItemBase;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class ItemDigitalManual extends ItemBase {

    public ItemDigitalManual(String name) {
        super(Rarmor.MODID, name, Rarmor.rarmorTab);
        Rarmor.proxy.addRenderer(new ItemStack(this), name);
    }

}
