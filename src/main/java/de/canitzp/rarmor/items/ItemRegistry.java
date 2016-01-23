package de.canitzp.rarmor.items;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.items.rfarmor.*;
import de.canitzp.util.items.ItemBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author canitzp
 */
public class ItemRegistry {

    public static ItemChainSaw ironChainsaw, diamondChainsaw;
    public static Item rfArmorBody, rfArmorHelmet, rfArmorLeggins, rfArmorBoots;
    public static Item moduleGenerator, moduleFastFurnace, moduleFlying, moduleSolarPanel;
    public static Item ribbonCable, electricalController, advancedEyeMatrix;
    public static Item digitalManual;

    public static void preInit(){
        Rarmor.logger.info("Registering Items");
        ironChainsaw = new ItemChainSaw(250000, 1500, 200, "ironChainsaw");
        diamondChainsaw = new ItemChainSaw(500000, 5000, 100, "diamondChainsaw");
        rfArmorBody = new ItemRFArmorBody().setCreativeTab(Rarmor.rarmorTab);
        rfArmorHelmet = new ItemRFArmorHelmet();
        rfArmorLeggins = new ItemRFArmorGeneric(ItemRFArmor.ArmorType.LEGGINS, 250000, 1500, "rfArmorLeggins").setCreativeTab(Rarmor.rarmorTab);
        rfArmorBoots = new ItemRFArmorGeneric(ItemRFArmor.ArmorType.SHOES, 250000, 1500, "rfArmorBoots").setCreativeTab(Rarmor.rarmorTab);
        moduleGenerator = new ItemModuleGenerator();
        moduleFastFurnace = new ItemModuleFurnaceSpeedUpgrade();
        moduleFlying = new ItemModuleFlying();
        moduleSolarPanel = new ItemModuleSolarPanel();
        ribbonCable = new Item().setUnlocalizedName(Rarmor.MODID + ".ribbonCable").setCreativeTab(Rarmor.rarmorTab);
        electricalController = new Item().setUnlocalizedName(Rarmor.MODID + ".electricalController").setCreativeTab(Rarmor.rarmorTab);
        advancedEyeMatrix = new ItemBase(Rarmor.MODID, "advancedEyeMatrix", Rarmor.rarmorTab);
        //digitalManual = new ItemDigitalManual("digitalManual");

        GameRegistry.registerItem(ribbonCable, "ribbonCable");
        GameRegistry.registerItem(electricalController, "electricalController");

        Rarmor.proxy.addRenderer(new ItemStack(ribbonCable), "ribbonCable");
        Rarmor.proxy.addRenderer(new ItemStack(electricalController), "electricalController");
        Rarmor.proxy.addRenderer(new ItemStack(advancedEyeMatrix), "advancedEyeMatrix");
    }

}
