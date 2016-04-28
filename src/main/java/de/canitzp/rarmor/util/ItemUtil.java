/*
 * This file 'ItemUtil.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.util;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class ItemUtil{

    public static Item getItemFromName(String name){
        ResourceLocation loc = new ResourceLocation(name);
        if(Item.REGISTRY.containsKey(loc)){
            return Item.REGISTRY.getObject(loc);
        } else {
            try{
                return Item.REGISTRY.getObjectById(Integer.parseInt(name));
            } catch(NumberFormatException numberformatexception){
                return Items.APPLE;
            }
        }
    }

}
