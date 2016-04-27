package de.canitzp.rarmor;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.hudExtensions.RarmorHud;
import de.canitzp.rarmor.event.EventHandler;
import de.canitzp.rarmor.integration.craftingTweaks.CraftingTweaksIntegration;
import de.canitzp.rarmor.items.ItemRegistry;
import de.canitzp.rarmor.items.rfarmor.modules.ItemModuleEffects;
import de.canitzp.rarmor.network.CommonProxy;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        String javaVersion = System.getProperty("java.version");
        javaVersion = javaVersion.substring(0, 3);
        if (!(javaVersion.equals("1.8") || javaVersion.equals("1.9"))){
            logger.error("You aren't using a compatible Java version to use Rarmor. This may crash your Game. Required-minimum: Java 1.8.0 Yours: " + System.getProperty("java.version"));
        }
        logger.info("Starting " + NAME + " " + VERSION + ". Thanks for using this Mod :)");
        RarmorProperties.preInit(event);
        rarmorTab = new CreativeTabs(NAME){
            @Override
            public Item getTabIconItem(){
                return ItemRegistry.rfArmorBody;
            }
        };
        ItemRegistry.preInit();
        if (event.getSide().isClient()) RarmorAPI.addAdvancedHud(new RarmorHud());
        logger.info("Finished PreInitialization");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        logger.info("Starting Initialization");
        NetworkHandler.init();
        EventHandler.init();
        proxy.registerRenderer();
        proxy.init();
        RecipeManager.init();
        logger.info("Finished Initialization");
        CraftingTweaksIntegration.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        initEffectsModule();
        if(event.getSide().isClient()){
            IResourceManager listener = Minecraft.getMinecraft().getResourceManager();
            if(listener instanceof IReloadableResourceManager){
                ((IReloadableResourceManager) listener).registerReloadListener(resourceManager -> {
                    ItemModuleEffects.postInit();
                });
            }
        }
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void missingMapping(FMLMissingMappingsEvent event){
        for (FMLMissingMappingsEvent.MissingMapping mapping : event.get()){
            if (mapping.name.startsWith("rarmor.")){
                mapping.remap(ItemUtil.getItemFromName(mapping.name.replace('.', ':')));
            }
        }
    }

    private static void initEffectsModule(){
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
                    if(potion.getName().substring(7).equals(entry.getKey())){
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
}
