package de.canitzp.rarmor.network;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.HashMap;

/**
 * @author canitzp
 */
public class CommonProxy{

    protected HashMap<ItemStack, String> textureMap = new HashMap<>();
    protected HashMap<ItemStack, String> specialTextures = new HashMap<>();

    public void init(){

    }

    public void postInit(FMLPostInitializationEvent event){
    }

    public void registerRenderer(){
    }

    public void addRenderer(ItemStack stack, String name){
        textureMap.put(stack, name);
    }

    public void addSpecialRenderer(ItemStack stack, String name){
        specialTextures.put(stack, name);
    }

}
