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
import de.canitzp.rarmor.event.CommonEvents;
import de.canitzp.rarmor.inventory.ContainerTypes;
import de.canitzp.rarmor.inventory.GuiHandler;
import de.canitzp.rarmor.item.ItemBase;
import de.canitzp.rarmor.item.ItemRegistry;
import de.canitzp.rarmor.misc.MethodHandler;
import de.canitzp.rarmor.module.ModuleRegistry;
import de.canitzp.rarmor.packet.PacketHandler;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

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
        ContainerTypes.CONTAINERS.register(modEventBus);
        
        // Register API
        RarmorAPI.methodHandler = new MethodHandler();
    }
    
    public void preInit(FML event){
        LOGGER.info("Starting "+MOD_NAME+"...");

        
        
        ItemRegistry.preInit();
        new GuiHandler();
        new UpdateChecker();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        ModuleRegistry.init();
        PacketHandler.init();
        new CommonEvents();
        proxy.init(event);
        for(Item item : ItemBase.ITEMS_TO_REGISTER){
            if(item instanceof IOreDictItem){
                List<String> names = ((IOreDictItem) item).getOreDictNames();
                if(names != null && !names.isEmpty()){
                    for(String name : names){
                        OreDictionary.registerOre(name, item);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerEvent(RegistryEvent.Register<Item> event){
        for(Item item : ItemBase.ITEMS_TO_REGISTER){
            event.getRegistry().register(item);
        }
    }

}
