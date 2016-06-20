package de.canitzp.rarmor;

import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.armor.RarmorColoringTab;
import de.canitzp.rarmor.armor.RarmorDependencyCrafting;
import de.canitzp.rarmor.armor.RarmorInventoryTab;
import de.canitzp.rarmor.armor.RarmorOverviewTab;
import de.canitzp.rarmor.network.CommonProxy;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
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
        List<Class<? extends IRarmorTab>> tabs = RarmorAPI.registeredTabs;
        if(!tabs.isEmpty()){
            RarmorAPI.registeredTabs.clear();
        }
        RarmorAPI.registerRarmorTab(RarmorOverviewTab.class);
        RarmorAPI.registerRarmorTab(RarmorInventoryTab.class);
        RarmorAPI.registerRarmorTab(RarmorColoringTab.class);
        this.launchSide = event.getSide();
        logger.info("Registering Items.");
        Registry.initItems(event);
        proxy.preInit(event);

        RarmorAPI.registerColor(0xFFFFFF, "White");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        proxy.init(event);
    }

}
