package de.canitzp.rarmor.items;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.items.rfarmor.*;
import de.canitzp.rarmor.util.items.ItemBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class ItemRegistry {

    public static ItemChainSaw ironChainsaw, diamondChainsaw;
    public static Item rfArmorBody, rfArmorHelmet, rfArmorLeggins, rfArmorBoots;
    public static Item moduleGenerator, moduleFastFurnace, moduleFlying, moduleSolarPanel, moduleMovement, moduleStat, moduleUnloader, moduleLoader, moduleDefense;
    public static Item ribbonCable, electricalController, advancedEyeMatrix;
    public static Item modularTool;

    public static void preInit(){
        Rarmor.logger.info("Registering Items");
        ironChainsaw = new ItemChainSaw(250000, 1500, 200, "ironChainsaw");
        diamondChainsaw = new ItemChainSaw(500000, 5000, 100, "diamondChainsaw");
        modularTool = new ItemModularTool(1000000, 8000, "modularTool");
        rfArmorBody = new ItemRFArmorBody();
        rfArmorHelmet = new ItemRFArmorHelmet();
        rfArmorLeggins = new ItemRFArmorGeneric(ItemRFArmor.ArmorType.LEGGINS, 250000, 1500, "rfArmorLeggins");
        rfArmorBoots = new ItemRFArmorGeneric(ItemRFArmor.ArmorType.SHOES, 250000, 1500, "rfArmorBoots");
        moduleGenerator = new ItemModuleGenerator();
        moduleFastFurnace = new ItemModuleFastFurnace();
        moduleFlying = new ItemModuleFlying();
        moduleSolarPanel = new ItemModuleSolarPanel();
        //moduleMovement = new ItemModuleMovement();
        //moduleStat = new ItemModuleStatistics();
        moduleUnloader = new ItemModuleEnergeticUnloader();
        moduleLoader = new ItemModuleEnergeticLoader();
        moduleDefense = new ItemModuleDefense();
        ribbonCable = new ItemBase(Rarmor.MODID, "ribbonCable", Rarmor.rarmorTab);
        electricalController = new ItemBase(Rarmor.MODID, "electricalController", Rarmor.rarmorTab);
        advancedEyeMatrix = new ItemBase(Rarmor.MODID, "advancedEyeMatrix", Rarmor.rarmorTab);

        Rarmor.proxy.addRenderer(new ItemStack(ribbonCable), "ribbonCable");
        Rarmor.proxy.addRenderer(new ItemStack(electricalController), "electricalController");
        Rarmor.proxy.addRenderer(new ItemStack(advancedEyeMatrix), "advancedEyeMatrix");
    }

}
