package de.canitzp.rarmor.network;

import net.minecraft.item.ItemStack;

import java.util.HashMap;

/**
 * @author canitzp
 */
public class CommonProxy {

    protected HashMap<ItemStack, String> textureMap = new HashMap<ItemStack, String>();

    public void init(){

    }

    public void registerRenderer(){}

    public void addRenderer(ItemStack stack, String name){
        textureMap.put(stack, name);
    }

}