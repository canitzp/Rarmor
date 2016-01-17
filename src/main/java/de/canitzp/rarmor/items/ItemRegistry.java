package de.canitzp.rarmor.items;

import cpw.mods.fml.common.registry.GameRegistry;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.items.rfarmor.ItemModuleGenerator;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmor;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorGeneric;
import net.minecraft.item.Item;

/**
 * @author canitzp
 */
public class ItemRegistry {

    public static Item ironChainsaw, diamondChainsaw;
    public static Item rfArmorBody, rfArmorHelmet, rfArmorLeggins, rfArmorBoots;
    public static Item moduleGenerator;
    public static Item ribbonCable, electricalController;

    public static void preInit(){
        Rarmor.logger.info("Registering Items");
        ironChainsaw = new ItemChainSaw(250000, 1500, 200, "ironChainsaw").setCreativeTab(Rarmor.rarmorTab);
        diamondChainsaw = new ItemChainSaw(500000, 5000, 100, "diamondChainsaw").setCreativeTab(Rarmor.rarmorTab);
        rfArmorBody = new ItemRFArmorBody().setCreativeTab(Rarmor.rarmorTab);
        rfArmorHelmet = new ItemRFArmorGeneric(ItemRFArmor.ArmorType.HEAD, 250000, 1500, "rfArmorHelmet").setCreativeTab(Rarmor.rarmorTab);
        rfArmorLeggins = new ItemRFArmorGeneric(ItemRFArmor.ArmorType.LEGGINS, 250000, 1500, "rfArmorLeggins").setCreativeTab(Rarmor.rarmorTab);
        rfArmorBoots = new ItemRFArmorGeneric(ItemRFArmor.ArmorType.SHOES, 250000, 1500, "rfArmorBoots").setCreativeTab(Rarmor.rarmorTab);
        moduleGenerator = new ItemModuleGenerator().setCreativeTab(Rarmor.rarmorTab);
        ribbonCable = new Item().setUnlocalizedName(Rarmor.MODID + ".ribbonCable").setTextureName(Rarmor.MODID + ":ribbonCable").setCreativeTab(Rarmor.rarmorTab);
        electricalController = new Item().setUnlocalizedName(Rarmor.MODID + ".electricalController").setTextureName(Rarmor.MODID + ":electricalController").setCreativeTab(Rarmor.rarmorTab);

        GameRegistry.registerItem(ribbonCable, "ribbonCable");
        GameRegistry.registerItem(electricalController, "electricalController");
    }

}
