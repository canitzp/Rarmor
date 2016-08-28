/*
 * This file ("ClientProxy.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.proxy;

import de.ellpeck.rarmor.mod.event.ClientEvents;
import de.ellpeck.rarmor.mod.item.ItemRegistry;
import de.ellpeck.rarmor.mod.module.protection.ActiveModuleProtection;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy{

    @Override
    public void preInit(FMLPreInitializationEvent event){
        this.addLocation(ItemRegistry.itemRarmorHelmet);
        this.addLocation(ItemRegistry.itemRarmorChest);
        this.addLocation(ItemRegistry.itemRarmorPants);
        this.addLocation(ItemRegistry.itemRarmorBoots);

        this.addLocation(ItemRegistry.itemModuleStorage);
        this.addLocation(ItemRegistry.itemModuleEnder);
        this.addLocation(ItemRegistry.itemModuleFurnace);
        this.addLocation(ItemRegistry.itemModuleSolar);
        this.addLocation(ItemRegistry.itemModuleGenerator);

        for(int i = 0; i < ActiveModuleProtection.TYPES.length; i++){
            ResourceLocation location = new ResourceLocation(ItemRegistry.itemModuleProtection.getRegistryName()+ActiveModuleProtection.TYPES[i]);
            this.addLocation(ItemRegistry.itemModuleProtection, i, location);
        }
    }

    @Override
    public void init(FMLInitializationEvent event){
        new ClientEvents();
    }

    private void addLocation(Item item){
        this.addLocation(item, 0);
    }

    private void addLocation(Item item, int meta){
        this.addLocation(item, meta, item.getRegistryName());
    }

    private void addLocation(Item item, int meta, ResourceLocation location){
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, "inventory"));
    }
}
