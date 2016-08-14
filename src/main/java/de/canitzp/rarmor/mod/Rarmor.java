/*
 * This file ("Rarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.mod.event.CommonEvents;
import de.canitzp.rarmor.mod.inventory.GuiHandler;
import de.canitzp.rarmor.mod.item.ItemRegistry;
import de.canitzp.rarmor.mod.misc.Config;
import de.canitzp.rarmor.mod.module.ModuleRegistry;
import de.canitzp.rarmor.mod.packet.PacketHandler;
import de.canitzp.rarmor.mod.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = RarmorAPI.MOD_ID, name = Rarmor.MOD_NAME, version = Rarmor.VERSION)
public final class Rarmor{

    public static final String MOD_NAME = "Rarmor";
    public static final String VERSION = "@VERSION@";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Mod.Instance(RarmorAPI.MOD_ID)
    public static Rarmor instance;

    @SidedProxy(clientSide = "de.canitzp.rarmor.mod.proxy.ClientProxy", serverSide = "de.canitzp.rarmor.mod.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        LOGGER.info("Starting "+MOD_NAME+"...");

        Config.init(event.getSuggestedConfigurationFile());
        ItemRegistry.preInit();
        new GuiHandler();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        ModuleRegistry.init();
        PacketHandler.init();
        new CommonEvents();
        proxy.init(event);
    }
}
