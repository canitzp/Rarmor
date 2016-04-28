/*
 * This file 'ItemRegistry.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorGeneric;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorHelmet;
import de.canitzp.rarmor.items.rfarmor.modules.*;
import de.canitzp.rarmor.util.items.ItemBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class ItemRegistry{

    public static ItemChainSaw ironChainsaw, diamondChainsaw;
    public static Item rfArmorBody, rfArmorHelmet, rfArmorLeggins, rfArmorBoots;
    public static Item moduleGenerator, moduleFastFurnace, moduleFlying, moduleSolarPanel, moduleUnloader, moduleLoader, moduleDefense, moduleModuleSplitter, moduleMovement, moduleEffects;
    public static Item ribbonCable, electricalController, advancedEyeMatrix;
    public static Item modularTool;
    public static Item moduleIronPickaxe;

    public static void preInit(){
        Rarmor.logger.info("Registering Items");
        ironChainsaw = new ItemChainSaw(250000, 1500, 200, "ironChainsaw");
        diamondChainsaw = new ItemChainSaw(500000, 5000, 100, "diamondChainsaw");
        ribbonCable = new ItemBase(Rarmor.MODID, "ribbonCable", Rarmor.rarmorTab);
        electricalController = new ItemBase(Rarmor.MODID, "electricalController", Rarmor.rarmorTab);
        advancedEyeMatrix = new ItemBase(Rarmor.MODID, "advancedEyeMatrix", Rarmor.rarmorTab);

        //Rarmor:
        rfArmorBody = new ItemRFArmorBody();
        rfArmorHelmet = new ItemRFArmorHelmet();
        rfArmorLeggins = new ItemRFArmorGeneric(EntityEquipmentSlot.LEGS, 250000, 1500, "rfArmorLeggins");
        rfArmorBoots = new ItemRFArmorGeneric(EntityEquipmentSlot.FEET, 250000, 1500, "rfArmorBoots");
        moduleGenerator = new ItemModuleGenerator();
        moduleFastFurnace = new ItemModuleFastFurnace();
        moduleFlying = new ItemModuleFlying();
        moduleSolarPanel = new ItemModuleSolarPanel();
        moduleUnloader = new ItemModuleEnergeticUnloader();
        moduleLoader = new ItemModuleEnergeticLoader();
        moduleDefense = new ItemModuleDefense();
        moduleModuleSplitter = new ItemModuleModuleSplitter();
        moduleMovement = new ItemModuleMovement();
        moduleEffects = new ItemModuleEffects();

        //ModularTool:
        //modularTool = new ItemModularTool(1000000, 8000, "modularTool");
        //moduleIronPickaxe = new ItemModuleIronPickaxe();


        Rarmor.proxy.addRenderer(new ItemStack(ribbonCable), "ribbonCable");
        Rarmor.proxy.addRenderer(new ItemStack(electricalController), "electricalController");
        Rarmor.proxy.addRenderer(new ItemStack(advancedEyeMatrix), "advancedEyeMatrix");
    }

}
