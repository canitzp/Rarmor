package de.canitzp.rarmor.network;

import de.canitzp.rarmor.Rarmor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

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
            if(entry.getKey() != null && entry.getKey().getItem() != null){
                System.out.println(entry.getKey().getItem());
                if(entry.getKey().getItem() instanceof ItemBlock){
                    registerBlock(entry.getKey().getItem(), entry.getValue());
                } else{
                    registerItem(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private void registerItem(ItemStack item, String blockName){
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item.getItem(), item.getItemDamage(), new ModelResourceLocation(Rarmor.MODID + ":" + blockName, "inventory"));
    }
    private void registerBlock(Item block, String blockName){
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(block, 0, new ModelResourceLocation(Rarmor.MODID + ":" + blockName, "inventory"));
    }



}
