package de.canitzp.rarmor;

import de.canitzp.rarmor.blocks.BlockRegistry;
import de.canitzp.rarmor.event.EventHandler;
import de.canitzp.rarmor.items.ItemRegistry;
import de.canitzp.rarmor.network.CommonProxy;
import de.canitzp.rarmor.network.NetworkHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
    public static CreativeTabs rarmorTab;
    public static final String CLIENTPROXY = "de.canitzp.rarmor.network.ClientProxy";
    public static final String SERVERPROXY = "de.canitzp.rarmor.network.CommonProxy";

    @Mod.Instance(MODID)
    public static Rarmor instance;
    @SidedProxy(clientSide = Rarmor.CLIENTPROXY, serverSide = Rarmor.SERVERPROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger.info("Starting " + NAME + " " + VERSION + ". Thanks for using this Mod :)");
        RarmorProperties.preInit(event);
        rarmorTab = new CreativeTabs(NAME) {@Override public Item getTabIconItem() {return ItemRegistry.rfArmorBody;}};
        BlockRegistry.preInit();
        ItemRegistry.preInit();
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
    }

    public static void registerItem(Item item, String name){
        item.setUnlocalizedName(MODID + "." + name);
        item.setRegistryName(name);
        item.setCreativeTab(rarmorTab);
        Rarmor.proxy.addRenderer(new ItemStack(item), name);
        GameRegistry.registerItem(item, name);
    }
}
