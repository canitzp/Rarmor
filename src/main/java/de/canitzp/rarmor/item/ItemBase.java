/*
 * This file ("ItemBase.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import de.canitzp.rarmor.IRenderItem;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.misc.CreativeTab;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class ItemBase extends Item implements IRenderItem{

    public static List<Item> ITEMS_TO_REGISTER = new ArrayList<>();

    public ItemBase(String name){
        this.setRegistryName(RarmorAPI.MOD_ID, name);
        ITEMS_TO_REGISTER.add(this);

        this.setUnlocalizedName(getRegistryName().toString());
        this.setCreativeTab(CreativeTab.INSTANCE);
    }

    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
