/*
 * This file ("Rarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.config.Config;
import de.canitzp.rarmor.inventory.RarmorContainerRegistry;
import de.canitzp.rarmor.inventory.gui.GuiRarmor;
import de.canitzp.rarmor.item.RarmorItemRegistry;
import de.canitzp.rarmor.misc.MethodHandler;
import de.canitzp.rarmor.module.ModuleRegistry;
import de.canitzp.rarmor.packet.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
@Mod(RarmorAPI.MOD_ID)
public final class Rarmor{

    public static final String MOD_NAME = "Rarmor";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static Rarmor INSTANCE;
    
    public Rarmor(){
        Rarmor.INSTANCE = this;
    
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    
        // Register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.FORGE_CONFIG_SPEC);
    
        // Registry handler
        RarmorItemRegistry.REGISTRY.register(modEventBus);
        RarmorContainerRegistry.REGISTRY.register(modEventBus);
        ScreenManager.registerFactory(RarmorContainerRegistry.RARMOR_CONTAINER.get(), GuiRarmor::new);
        
        // Register Network
        PacketHandler.init();
        
        // Register API
        RarmorAPI.methodHandler = new MethodHandler();
        ModuleRegistry.init();
    
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft.getInstance().getItemColors().register(new IItemColor() {
                @Override
                public int getColor(ItemStack stack, int tintIndex){
                    return stack.hasTag() && stack.getTag().contains("Color", Constants.NBT.TAG_INT) ? stack.getTag().getInt("Color") : 0xFFFFFFFF;
                }
            }, RarmorItemRegistry.itemRarmorBoots.get(), RarmorItemRegistry.itemRarmorChest.get(), RarmorItemRegistry.itemRarmorHelmet.get(), RarmorItemRegistry.itemRarmorPants.get());
        });
    }
    
}
