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
import de.canitzp.rarmor.compat.Compat;
import de.canitzp.rarmor.config.Config;
import de.canitzp.rarmor.event.CommonEvents;
import de.canitzp.rarmor.inventory.GuiHandler;
import de.canitzp.rarmor.item.ItemBase;
import de.canitzp.rarmor.item.ItemRegistry;
import de.canitzp.rarmor.misc.MethodHandler;
import de.canitzp.rarmor.module.ModuleRegistry;
import de.canitzp.rarmor.packet.PacketHandler;
import de.canitzp.rarmor.proxy.IProxy;
import de.canitzp.rarmor.update.UpdateChecker;
import de.canitzp.rarmor.crafting.CraftingRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
@Mod(modid = RarmorAPI.MOD_ID, name = Rarmor.MOD_NAME, version = Rarmor.VERSION, guiFactory = "de.canitzp.rarmor.config.ConfigGuiFactory")
public final class Rarmor{

    public static final String MOD_NAME = "Rarmor";
    public static final String VERSION = "@VERSION@";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Mod.Instance(RarmorAPI.MOD_ID)
    public static Rarmor instance;

    @SidedProxy(clientSide = "de.canitzp.rarmor.proxy.ClientProxy", serverSide = "de.canitzp.rarmor.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        LOGGER.info("Starting "+MOD_NAME+"...");

        RarmorAPI.methodHandler = new MethodHandler();

        new Config(event.getSuggestedConfigurationFile());
        ItemRegistry.preInit();
        Compat.preInit();
        new GuiHandler();
        new UpdateChecker();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        ModuleRegistry.init();
        PacketHandler.init();
        CraftingRegistry.init();
        new CommonEvents();
        proxy.init(event);
    }

    @SubscribeEvent
    public static void registerEvent(RegistryEvent.Register<Item> event){
        for(Item item : ItemBase.ITEMS_TO_REGISTER){
            event.getRegistry().register(item);
        }
    }

}
