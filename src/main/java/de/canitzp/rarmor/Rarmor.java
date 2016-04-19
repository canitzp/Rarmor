package de.canitzp.rarmor;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.hudExtensions.RarmorHud;
import de.canitzp.rarmor.blocks.BlockRegistry;
import de.canitzp.rarmor.event.EventHandler;
import de.canitzp.rarmor.integration.craftingTweaks.CraftingTweaksIntegration;
import de.canitzp.rarmor.items.ItemRegistry;
import de.canitzp.rarmor.network.CommonProxy;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author canitzp
 */
@Mod(modid = Rarmor.MODID, name = Rarmor.NAME, version = Rarmor.VERSION)
public class Rarmor {
    public static final String MODID = "rarmor", NAME = "Rarmor", VERSION = "@VERSION";
    public static final Logger logger = LogManager.getLogger(NAME);
    public static final String CLIENTPROXY = "de.canitzp.rarmor.network.ClientProxy";
    public static final String SERVERPROXY = "de.canitzp.rarmor.network.CommonProxy";
    public static CreativeTabs rarmorTab;
    @Mod.Instance(MODID)
    public static Rarmor instance;
    @SidedProxy(clientSide = Rarmor.CLIENTPROXY, serverSide = Rarmor.SERVERPROXY)
    public static CommonProxy proxy;

    public static void registerItem(Item item, String name) {
        item.setUnlocalizedName(MODID + ":" + name);
        item.setRegistryName(name);
        item.setCreativeTab(rarmorTab);
        Rarmor.proxy.addRenderer(new ItemStack(item), name);
        GameRegistry.register(item);
    }

    public static void registerBlock(Block block, String name) {
        block.setUnlocalizedName(MODID + ":" + name);
        block.setRegistryName(name);
        block.setCreativeTab(rarmorTab);
        Rarmor.proxy.addRenderer(new ItemStack(block), name);
        GameRegistry.register(block);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        String javaVersion = System.getProperty("java.version");
        javaVersion = javaVersion.substring(0, 3);
        if(!(javaVersion.equals("1.8") || javaVersion.equals("1.9"))){
            logger.error("You aren't using a compatible Java version to use Rarmor. This may crash your Game. Required-minimum: Java 1.8.0 Yours: " + System.getProperty("java.version"));
        }
        logger.info("Starting " + NAME + " " + VERSION + ". Thanks for using this Mod :)");
        RarmorProperties.preInit(event);
        rarmorTab = new CreativeTabs(NAME) {
            @Override
            public Item getTabIconItem() {
                return ItemRegistry.rfArmorBody;
            }
        };
        BlockRegistry.preInit();
        ItemRegistry.preInit();
        if (event.getSide().isClient()) RarmorAPI.addAdvancedHud(new RarmorHud());
        logger.info("Finished PreInitialization");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
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
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void missingMapping(FMLMissingMappingsEvent event) {
        for (FMLMissingMappingsEvent.MissingMapping mapping : event.get()) {
            if (mapping.name.startsWith("rarmor.")) {
                mapping.remap(ItemUtil.getItemFromName(mapping.name.replace('.', ':')));
            }
        }
    }
}
