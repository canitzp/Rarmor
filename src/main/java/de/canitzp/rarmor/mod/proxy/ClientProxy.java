/*
 * This file ("ClientProxy.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.proxy;

import de.canitzp.rarmor.mod.item.ItemRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy{

    @Override
    public void preInit(FMLPreInitializationEvent event){
        this.addLocation(ItemRegistry.itemRarmorHelmet);
        this.addLocation(ItemRegistry.itemRarmorChest);
        this.addLocation(ItemRegistry.itemRarmorPants);
        this.addLocation(ItemRegistry.itemRarmorBoots);
    }

    private void addLocation(Item item){
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
