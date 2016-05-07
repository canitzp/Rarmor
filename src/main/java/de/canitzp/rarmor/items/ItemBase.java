/*
 * This file 'ItemBase.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items;

import de.canitzp.rarmor.Rarmor;
import net.minecraft.item.Item;

/**
 * @author canitzp
 */
public class ItemBase extends Item{

    public ItemBase(String name){
        Rarmor.registerItem(this, name);
    }

}
