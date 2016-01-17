package de.canitzp.rarmor.network;

import com.google.common.collect.Maps;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.items.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class ClientProxy extends CommonProxy {



    @Override
    public void init(){
        GuiOpenChanger guiChanger = new GuiOpenChanger();
        MinecraftForge.EVENT_BUS.register(guiChanger);
        HudEvent hud = new HudEvent();
        MinecraftForge.EVENT_BUS.register(hud);
    }

    @Override
    public void registerRenderer(){
        for(Map.Entry<ItemStack, String> entry : textureMap.entrySet()){
            registerItem(entry.getKey(), entry.getValue());
        }
    }

    private void registerItem(ItemStack item, String blockName){
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item.getItem(), item.getItemDamage(), new ModelResourceLocation(Rarmor.MODID + ":" + blockName, "inventory"));
    }



}
