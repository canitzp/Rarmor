/*
 * This file 'CommonProxy.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.network;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;

/**
 * @author canitzp
 */
public class CommonProxy{

    protected HashMap<ItemStack, String> textureMap = new HashMap<>();
    protected HashMap<ItemStack, String> specialTextures = new HashMap<>();

    public void preInit(FMLPreInitializationEvent event){}

    public void init(FMLInitializationEvent event){}

    public void postInit(FMLPostInitializationEvent event){}

    public void registerRenderer(){}

    public void addRenderer(ItemStack stack, String name){
        textureMap.put(stack, name);
    }

    public void addSpecialRenderer(ItemStack stack, String name){
        specialTextures.put(stack, name);
    }

}
