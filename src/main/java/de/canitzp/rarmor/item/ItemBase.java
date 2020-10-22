/*
 * This file ("ItemBase.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import de.canitzp.rarmor.IOreDictItem;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.misc.CreativeTab;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBase extends Item implements IOreDictItem{
    
    private List<String> oreDictNames = new ArrayList<>();
    
    public ItemBase(){
        this(new Properties());
    }
    
    public ItemBase(Properties properties){
        super(properties.group(CreativeTab.INSTANCE));
    }
    
    public ItemBase addOreDict(String... names){
        this.oreDictNames.addAll(Arrays.asList(names));
        return this;
    }
    
    @Override
    public List<String> getOreDictNames(){
        return this.oreDictNames;
    }
}
