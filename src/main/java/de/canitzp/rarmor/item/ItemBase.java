/*
 * This file ("ItemBase.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import de.canitzp.rarmor.misc.CreativeTab;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemBase extends Item {
    
    public ItemBase(){
        this(new Properties());
    }
    
    public ItemBase(Properties properties){
        super(properties.group(CreativeTab.INSTANCE));
    }
    
}
