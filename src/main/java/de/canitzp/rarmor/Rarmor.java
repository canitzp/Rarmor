package de.canitzp.rarmor;

import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.RarmorValues;
import de.canitzp.rarmor.armor.*;
import de.canitzp.rarmor.network.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.RecipeSorter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author canitzp
 */
@Mod(modid = Rarmor.MODID, name = Rarmor.NAME, version = Rarmor.VERSION)
public class Rarmor{

    public static final String NAME = "Rarmor";
    public static final String MODID = "rarmor";
    public static final String VERSION = "@VERSION@";

    public static final Logger logger = LogManager.getLogger(NAME);

    public Side launchSide;

    public static final ItemArmor.ArmorMaterial rarmorMaterial = EnumHelper.addArmorMaterial("rarmor", "", 0, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);

    @Mod.Instance
    public static Rarmor instance;
    @SidedProxy(clientSide = "de.canitzp.rarmor.network.ClientProxy", serverSide ="de.canitzp.rarmor.network.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger.info("Rarmor: The way to a peaceful world. Version:" + VERSION);

        initConfigValues(event);

        List<Class<? extends IRarmorTab>> tabs = RarmorAPI.registeredTabs;
        if(!tabs.isEmpty()){
            RarmorAPI.registeredTabs.clear();
        }
        RarmorAPI.registerRarmorTab(RarmorOverviewTab.class);
        RarmorAPI.registerRarmorTab(RarmorInventoryTab.class);
        RarmorAPI.registerRarmorTab(RarmorColoringTab.class);
        RarmorAPI.registerRarmorTab(RarmorCoalGeneratorTab.class);
        registerColors();
        this.launchSide = event.getSide();
        logger.info("Registering Items.");
        Registry.initItems(event);
        proxy.preInit(event);

        GameRegistry.addRecipe(new RarmorDependencyCrafting(Pair.of("DependencyChest", true), Blocks.CHEST, Blocks.CHEST, Blocks.CHEST));

        RecipeSorter.register("RarmorDependencyCrafting", RarmorDependencyCrafting.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        proxy.init(event);
    }

    public void missingMapping(FMLMissingMappingsEvent event){
        for(FMLMissingMappingsEvent.MissingMapping mapping : event.get()){
            if(mapping.name.startsWith("rarmor:")){
                switch(mapping.name){
                    case "rarmor:rarmorChestplate":{
                        mapping.remap(Registry.rarmorChestplate);
                        break;
                    }
                    case "rarmor:rarmorHelmet":{
                        mapping.remap(Registry.rarmorHelmet);
                        break;
                    }
                    case "rarmor:rarmorLeggins":{
                        mapping.remap(Registry.rarmorLeggins);
                        break;
                    }
                    case "rarmor:rarmorBoots":{
                        mapping.remap(Registry.rarmorBoots);
                        break;
                    }
                }
            }
        }
    }

    private void registerColors(){
        for(EnumDyeColor color : EnumDyeColor.values()){
            RarmorAPI.registerColor(color.getMapColor().colorValue, StringUtils.capitalize(color.getName().replace("_", " ")));
        }
    }

    private void initConfigValues(FMLPreInitializationEvent event){
        RarmorValues.rarmorMaxEnergy = 250000;
        RarmorValues.rarmorMaxTransfer = 25000;
        RarmorValues.generatorTabTickValue = 40;
    }

}
