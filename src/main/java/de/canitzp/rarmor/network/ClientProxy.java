package de.canitzp.rarmor.network;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.util.MinecraftUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * @author canitzp
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void init(){

    }

    @Override
    public void registerRenderer(){
        Rarmor.logger.info("Register Renderer");
        //ModelBakery.registerItemVariants(ItemRegistry.modularTool, new ResourceLocation(Rarmor.MODID, "modularTool"));
        for(Map.Entry<ItemStack, String> entry : textureMap.entrySet()){
            if(entry.getKey() != null && entry.getKey().getItem() != null){
                if(entry.getKey().getItem() instanceof ItemBlock){
                    registerBlock(entry.getKey().getItem(), entry.getValue());
                } else{
                    registerItem(entry.getKey(), entry.getValue());
                }
            }
        }
        for(Map.Entry<ItemStack, String> entry : specialTextures.entrySet()){
            if(entry.getKey() != null && entry.getKey().getItem() != null){
                if(entry.getKey().getItem() instanceof ItemBlock){
                    registerBlock(entry.getKey().getItem(), entry.getValue() + (entry.getKey().getMetadata() == 0 ? "" : entry.getKey().getMetadata()));
                } else{
                    registerItem(entry.getKey(), entry.getValue() + (entry.getKey().getMetadata() == 0 ? "" : entry.getKey().getMetadata()));
                }
            }
        }
    }

    private void registerItem(ItemStack item, String blockName){
        MinecraftUtil.getMinecraft().getRenderItem().getItemModelMesher().register(item.getItem(), item.getMetadata(), new ModelResourceLocation(Rarmor.MODID + ":" + blockName, "inventory"));
    }
    private void registerBlock(Item block, String blockName){
        MinecraftUtil.getMinecraft().getRenderItem().getItemModelMesher().register(block, 0, new ModelResourceLocation(Rarmor.MODID + ":" + blockName, "inventory"));
    }

}
