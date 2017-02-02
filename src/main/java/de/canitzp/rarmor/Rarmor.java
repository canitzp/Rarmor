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
import de.canitzp.rarmor.item.ItemRegistry;
import de.canitzp.rarmor.misc.MethodHandler;
import de.canitzp.rarmor.module.ModuleRegistry;
import de.canitzp.rarmor.packet.PacketHandler;
import de.canitzp.rarmor.proxy.IProxy;
import de.canitzp.rarmor.update.UpdateChecker;
import de.canitzp.rarmor.crafting.CraftingRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    @Mod.EventHandler
    public void mapping(FMLMissingMappingsEvent event){
        for(FMLMissingMappingsEvent.MissingMapping mapping : event.getAll()){
            if(mapping.resourceLocation.getResourceDomain().equals("rarmor")){
                String r = mapping.resourceLocation.getResourcePath();
                Item item = null;
                switch(r){
                    case "itemsolarcell": item = ItemRegistry.itemSolarCell; break;
                    case "itemmodulefurnace": item = ItemRegistry.itemModuleFurnace; break;
                    case "itemmoduleprotectiondiamond": item = ItemRegistry.itemModuleProtectionDiamond; break;
                    case "itemrarmorboots": item = ItemRegistry.itemRarmorBoots; break;
                    case "itemmodulegenerator": item = ItemRegistry.itemModuleGenerator; break;
                    case "itemconnector": item = ItemRegistry.itemConnector; break;
                    case "itemmodulespeed": item = ItemRegistry.itemModuleSpeed; break;
                    case "itemrarmorhelmet": item = ItemRegistry.itemRarmorHelmet; break;
                    case "itemmodulejump": item = ItemRegistry.itemModuleJump; break;
                    case "itembattery": item = ItemRegistry.itemBattery; break;
                    case "itemrarmorpants": item = ItemRegistry.itemRarmorPants; break;
                    case "itemcontrolcircuit": item = ItemRegistry.itemControlCircuit; break;
                    case "itemmodulemovement": item = ItemRegistry.itemModuleMovement; break;
                    case "itemrarmorchest": item = ItemRegistry.itemRarmorChest; break;
                    case "itemmoduleender": item = ItemRegistry.itemModuleEnder; break;
                    case "itemmoduleprotectioniron": item = ItemRegistry.itemModuleProtectionIron; break;
                    case "itemmodulesolar": item = ItemRegistry.itemModuleSolar; break;
                    case "itemwire": item = ItemRegistry.itemWire; break;
                    case "itemwirecutter": item = ItemRegistry.itemWireCutter; break;
                    case "itemmodulestorage": item = ItemRegistry.itemModuleStorage; break;
                    case "itemmoduleprotectiongold": item = ItemRegistry.itemModuleProtectionGold; break;
                    case "itemgenerator": item = ItemRegistry.itemGenerator; break;
                }
                mapping.remap(item);
            }
        }
    }
}
