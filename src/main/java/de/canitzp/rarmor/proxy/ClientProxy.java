/*
 * This file ("ClientProxy.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.proxy;

import de.canitzp.rarmor.item.ItemRegistry;
import de.canitzp.rarmor.event.ClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy{

    public static void addLocation(Item item){
        addLocation(item, 0);
    }

    public static void addLocation(Item item, int meta){
        addLocation(item, meta, item.getRegistryName());
    }

    public static void addLocation(Item item, int meta, ResourceLocation location){
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, "inventory"));
    }

    @Override
    public void preInit(FMLPreInitializationEvent event){
        ItemRegistry.preInitClient();
    }

    @Override
    public void init(FMLInitializationEvent event){
        new ClientEvents();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor(){
            @Override
            public int getColorFromItemstack(ItemStack stack, int tintIndex){
                return stack.hasTagCompound() && stack.getTagCompound().hasKey("Color", 3) ? stack.getTagCompound().getInteger("Color") : 0xFFFFFFFF;
            }
        }, ItemRegistry.itemRarmorBoots, ItemRegistry.itemRarmorChest, ItemRegistry.itemRarmorHelmet, ItemRegistry.itemRarmorPants);
    }
}
