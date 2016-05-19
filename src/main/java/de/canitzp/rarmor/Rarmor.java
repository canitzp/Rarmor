/*
 * This file 'Rarmor.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.event.EventHandler;
import de.canitzp.rarmor.integration.ActuallyAdditionsIntegration;
import de.canitzp.rarmor.integration.CraftingTweaksIntegration;
import de.canitzp.rarmor.items.ItemRegistry;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.items.rfarmor.modules.ItemModuleEffects;
import de.canitzp.rarmor.network.CommonProxy;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.util.ItemUtil;
import de.canitzp.rarmor.util.MinecraftUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
@Mod(modid = Rarmor.MODID, name = Rarmor.NAME, version = Rarmor.VERSION)
public class Rarmor{
    public static final String MODID = "rarmor", NAME = "Rarmor", VERSION = "@VERSION";
    public static final Logger logger = LogManager.getLogger(NAME);
    public static final String CLIENTPROXY = "de.canitzp.rarmor.network.ClientProxy";
    public static final String SERVERPROXY = "de.canitzp.rarmor.network.CommonProxy";
    public static CreativeTabs rarmorTab;
    @Mod.Instance(MODID)
    public static Rarmor instance;
    @SidedProxy(clientSide = Rarmor.CLIENTPROXY, serverSide = Rarmor.SERVERPROXY)
    public static CommonProxy proxy;

    public static void registerItem(Item item, String name){
        item.setUnlocalizedName(MODID + ":" + name);
        item.setRegistryName(name);
        item.setCreativeTab(rarmorTab);
        Rarmor.proxy.addRenderer(new ItemStack(item), name);
        GameRegistry.register(item);
    }

    public static void initEffectsModule(){
        //Read Special Module energy costs:
        Map<String, Integer> map = new HashMap<>();
        for(String string : RarmorProperties.getStringArray("ActivatedModulesWithEnergyPerTick")){
            String[] array = string.split("@");
            if(array.length == 2){
                if(RarmorProperties.rarmorProperties.isEntryInteger(array[1])){
                    map.put(array[0], Integer.parseInt(array[1]));
                } else {
                    map.put(array[0], -1);
                }
            }
        }
        Map<Potion, Boolean> defaultAdd = new HashMap<>();
        for(Potion potion : ForgeRegistries.POTIONS){
            defaultAdd.put(potion, false);
        }
        if(RarmorProperties.getBoolean("YouTubeMode")){
            map.put("nightVision", 0);
        }
        for(Potion potion : ForgeRegistries.POTIONS){
            if(!defaultAdd.get(potion)){
                for(Map.Entry<String, Integer> entry : map.entrySet()){
                    if((potion.getName().startsWith("effect.") || potion.getName().startsWith("potion.")) && potion.getName().substring(7).equals(entry.getKey())){
                        if(entry.getValue() >= 0){
                            ItemModuleEffects.addPotionEffect(potion, entry.getValue());
                            defaultAdd.replace(potion, true);
                        } else {
                            defaultAdd.replace(potion, true);
                        }
                    } else if(!potion.getName().contains(".") && potion.getName().equals(entry.getKey())) {
                        if(entry.getValue() >= 0){
                            ItemModuleEffects.addPotionEffect(potion, entry.getValue());
                            defaultAdd.replace(potion, true);
                        } else {
                            defaultAdd.replace(potion, true);
                        }
                    }
                }
            }
        }
        for(Map.Entry<Potion, Boolean> entry : defaultAdd.entrySet()){
            if(!entry.getValue()){
                ItemModuleEffects.addPotionEffect(entry.getKey(), RarmorProperties.getInteger("DefaultModuleEffectEnergyTick"));
            }
        }
        ItemModuleEffects.postInit();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        String javaVersion = System.getProperty("java.version");
        javaVersion = javaVersion.substring(0, 3);
        if(!(javaVersion.equals("1.8") || javaVersion.equals("1.9"))){
            logger.error("You aren't using a compatible Java version to use Rarmor. This may crash your Game. Required-minimum: Java 1.8.0 Yours: " + System.getProperty("java.version"));
        }
        logger.info("[Rarmor] Starting " + NAME + " " + VERSION + ". Thanks for using this Mod :)");
        RarmorProperties.preInit(event);
        rarmorTab = new CreativeTabs(NAME){
            @Override
            public Item getTabIconItem(){
                return ItemRegistry.rarmorChestplate;
            }

            @Override
            public ItemStack getIconItemStack(){
                ItemStack stack = new ItemStack(ItemRegistry.rarmorChestplate);
                NBTUtil.setInteger(stack, "color", ItemRFArmorBody.tabColor.colorValue);
                return stack;
            }
        };
        ItemRegistry.preInit();
        if(event.getSide().isClient()) RarmorAPI.addAdvancedHud(new RarmorHud());
        proxy.preInit(event);
        logger.info("[Rarmor] Finished PreInitialization");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        logger.info("[Rarmor] Starting Initialization");
        NetworkHandler.init();
        EventHandler.init(event);
        proxy.registerRenderer();
        proxy.init(event);
        RecipeManager.init();
        CraftingTweaksIntegration.init();
        logger.info("[Rarmor] Finished Initialization");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        logger.info("[Rarmor] Starting PostInitialization");
        initEffectsModule();
        if(Loader.isModLoaded("actuallyadditions")) {
            for(ModContainer mod : ModAPIManager.INSTANCE.getAPIList()){
                if("actuallyadditionsapi".equals(mod.getModId())){
                    if(Integer.parseInt(mod.getVersion()) >= 11){
                        ActuallyAdditionsIntegration.postInit(event);
                        logger.info("Loaded ActuallyAdditions Integration");
                    }
                }
            }
        }
        proxy.postInit(event);
        logger.info("[Rarmor] Finished PostInitialization");
    }

    @Mod.EventHandler
    public void missingMapping(FMLMissingMappingsEvent event){
        for(FMLMissingMappingsEvent.MissingMapping mapping : event.get()){
            if(mapping.name.startsWith("rarmor.")){
                mapping.remap(ItemUtil.getItemFromName(mapping.name.replace('.', ':')));
            }
            if(mapping.name.equals("rarmor:rfArmorBoots")){
                mapping.remap(ItemRegistry.rarmorBoots);
            }
            if(mapping.name.equals("rarmor:rfArmorHelmet")){
                mapping.remap(ItemRegistry.rarmorHelmet);
            }
            if(mapping.name.equals("rarmor:rfArmorBody")){
                mapping.remap(ItemRegistry.rarmorChestplate);
            }
            if(mapping.name.equals("rarmor:rfArmorLeggins")){
                mapping.remap(ItemRegistry.rarmorLeggins);
            }
        }
    }

}
