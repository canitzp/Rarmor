package de.canitzp.rarmor;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import de.canitzp.api.util.PlayerUtil;
import de.canitzp.rarmor.inventory.GuiHandler;
import de.canitzp.rarmor.items.ItemRegistry;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorGeneric;
import de.canitzp.api.packets.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author canitzp
 */
@Mod(modid = Rarmor.MODID, name = Rarmor.NAME, version = Rarmor.VERSION)
public class Rarmor {
    public static final String MODID = "rarmor", NAME = "Rarmor", VERSION = "a1";
    public static final Logger logger = LogManager.getLogger(NAME);
    public static final ItemArmor.ArmorMaterial rfarmor = EnumHelper.addArmorMaterial("RFArmor", 1, new int[]{1, 1, 1, 1}, 1);
    public static CreativeTabs rarmorTab;

    @Mod.Instance(MODID)
    public static Rarmor instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger.info("Starting " + NAME + " " + VERSION + " with ModID: " + MODID + ". Thanks for using this Mod :)");
        rarmorTab = new CreativeTabs(NAME) {@Override public Item getTabIconItem() {return ItemRegistry.rfArmorBody;}};
        PacketHandler.preInit();
        ItemRegistry.preInit();
        logger.info("Finished PreInitialization");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        logger.info("Starting Initialization");
        Rarmor guiChanger = new Rarmor();
        FMLCommonHandler.instance().bus().register(guiChanger);
        MinecraftForge.EVENT_BUS.register(guiChanger);
        NetworkRegistry.INSTANCE.registerGuiHandler(MODID, new GuiHandler());
        RecipeManager.init();
        logger.info("Finished Initialization");
    }

    @SubscribeEvent
    public void overrideGui(GuiOpenEvent event){
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player != null){
            ItemStack head = player.getCurrentArmor(3);
            ItemStack body = player.getCurrentArmor(2);
            ItemStack leggins = player.getCurrentArmor(1);
            ItemStack boots = player.getCurrentArmor(0);
            if(head != null && body != null && leggins != null && boots != null){
                if(head.getItem() instanceof ItemRFArmorGeneric && body.getItem() instanceof ItemRFArmorBody && leggins.getItem() instanceof ItemRFArmorGeneric && boots.getItem() instanceof ItemRFArmorGeneric){
                    if(event.gui instanceof GuiInventory && (body.stackTagCompound == null || !body.stackTagCompound.getBoolean("click"))){
                        if(body.stackTagCompound != null) body.stackTagCompound.setBoolean("click", false);
                        event.setCanceled(true);
                        PlayerUtil.openInventoryFromClient(player, instance, GuiHandler.RFArmorGui);
                    } else if(body.stackTagCompound != null) body.stackTagCompound.setBoolean("click", false);
                }
            }
        }
    }
}