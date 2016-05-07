/*
 * This file 'ClientProxy.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.network;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.Colors;
import de.canitzp.rarmor.items.ItemRegistry;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmor;
import de.canitzp.rarmor.items.rfarmor.modules.ItemModuleEffects;
import de.canitzp.rarmor.util.MinecraftUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
@SuppressWarnings("ConstantConditions")
public class ClientProxy extends CommonProxy{

    public static Map<String, Pair<String, Colors>> specialPlayers = new HashMap<>();

    @Override
    public void init(FMLInitializationEvent event){
        specialPlayers.put("Xogue", Pair.of("You helped me to create a better Mod.", Colors.CYAN));
        specialPlayers.put("canitzp", Pair.of("...", Colors.RED));
        specialPlayers.put("Ellpeck", Pair.of("You helped me a lot with everything stuff I developed.", Colors.ELLPECKGREEN));
        specialPlayers.put("DogBlesseD", Pair.of("'love the rarmor....rarmor is love...rarmor is life'", Colors.DOGBLESSEDBLUE));
        registerColoring();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event){
        IResourceManager listener = Minecraft.getMinecraft().getResourceManager();
        if(listener instanceof IReloadableResourceManager){
            ((IReloadableResourceManager) listener).registerReloadListener(new IResourceManagerReloadListener(){
                @Override
                public void onResourceManagerReload(IResourceManager resourceManager){
                    ItemModuleEffects.effectBoxes.clear();
                    ItemModuleEffects.energyEffect.clear();
                    Rarmor.initEffectsModule();

                    registerColoring();
                }
            });
        }
    }

    @Override
    public void registerRenderer(){
        Rarmor.logger.info("Register Renderer");
        for(Map.Entry<ItemStack, String> entry : textureMap.entrySet()){
            if(entry.getKey() != null && entry.getKey().getItem() != null){
                if(entry.getKey().getItem() instanceof ItemBlock){
                    registerBlock(entry.getKey().getItem(), entry.getValue());
                } else {
                    registerItem(entry.getKey(), entry.getValue());
                }
            }
        }
        for(Map.Entry<ItemStack, String> entry : specialTextures.entrySet()){
            if(entry.getKey() != null && entry.getKey().getItem() != null){
                if(entry.getKey().getItem() instanceof ItemBlock){
                    registerBlock(entry.getKey().getItem(), entry.getValue() + (entry.getKey().getMetadata() == 0 ? "" : entry.getKey().getMetadata()));
                } else {
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

    public void registerColoring(){
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                if(stack.getItem() instanceof ItemRFArmor){
                    return NBTUtil.getInteger(stack, "color");
                }
                return Colors.WHITE.colorValue;
            }
        }, ItemRegistry.rarmorChestplate, ItemRegistry.rarmorBoots, ItemRegistry.rarmorLeggins, ItemRegistry.rarmorHelmet);
    }

}
