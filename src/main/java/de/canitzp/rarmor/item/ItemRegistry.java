/*
 * This file ("ItemRegistry.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import de.canitzp.rarmor.module.generator.ItemModuleGenerator;
import de.canitzp.rarmor.module.compound.ItemModuleCompound;
import de.canitzp.rarmor.module.ender.ItemModuleEnder;
import de.canitzp.rarmor.module.furnace.ItemModuleFurnace;
import de.canitzp.rarmor.module.jump.ActiveModuleJump;
import de.canitzp.rarmor.module.jump.ItemModuleJump;
import de.canitzp.rarmor.module.protection.ActiveModuleProtectionDiamond;
import de.canitzp.rarmor.module.protection.ActiveModuleProtectionGold;
import de.canitzp.rarmor.module.protection.ActiveModuleProtectionIron;
import de.canitzp.rarmor.module.protection.ItemModuleProtection;
import de.canitzp.rarmor.module.solar.ItemModuleSolar;
import de.canitzp.rarmor.module.speed.ActiveModuleSpeed;
import de.canitzp.rarmor.module.speed.ItemModuleSpeed;
import de.canitzp.rarmor.module.storage.ItemModuleStorage;
import de.canitzp.rarmor.proxy.ClientProxy;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ItemRegistry{

    public static Item itemRarmorHelmet;
    public static Item itemRarmorChest;
    public static Item itemRarmorPants;
    public static Item itemRarmorBoots;

    public static Item itemWireCutter;
    public static Item itemControlCircuit;
    public static Item itemWire;
    public static Item itemConnector;
    public static Item itemSolarCell;
    public static Item itemGenerator;
    public static Item itemBattery;

    public static Item itemModuleStorage;
    public static Item itemModuleEnder;
    public static Item itemModuleFurnace;
    public static Item itemModuleSolar;
    public static Item itemModuleGenerator;
    public static ItemModuleSpeed itemModuleSpeed;
    public static ItemModuleJump itemModuleJump;
    public static Item itemModuleMovement;

    public static Item itemModuleProtectionIron;
    public static Item itemModuleProtectionGold;
    public static Item itemModuleProtectionDiamond;

    public static void preInit(){
        itemRarmorHelmet = new ItemRarmor("item_rarmor_helmet", EntityEquipmentSlot.HEAD);
        itemRarmorChest = new ItemRarmorChest("item_rarmor_chest");
        itemRarmorPants = new ItemRarmor("item_rarmor_pants", EntityEquipmentSlot.LEGS);
        itemRarmorBoots = new ItemRarmor("item_rarmor_boots", EntityEquipmentSlot.FEET);

        itemWireCutter = new ItemWireCutter("item_wire_cutter");
        itemControlCircuit = new ItemBase("item_control_circuit");
        itemWire = new ItemBase("item_wire");
        itemConnector = new ItemBase("item_connector");
        itemSolarCell = new ItemBase("item_solar_cell");
        itemGenerator = new ItemBase("item_generator");
        itemBattery = new ItemBattery("item_battery");

        itemModuleStorage = new ItemModuleStorage("storage");
        itemModuleEnder = new ItemModuleEnder("ender");
        itemModuleFurnace = new ItemModuleFurnace("furnace");
        itemModuleSolar = new ItemModuleSolar("solar");
        itemModuleGenerator = new ItemModuleGenerator("generator");
        itemModuleSpeed = new ItemModuleSpeed("speed");
        itemModuleJump = new ItemModuleJump("jump");
        itemModuleMovement = new ItemModuleCompound("movement", new String[]{ActiveModuleSpeed.IDENTIFIER, ActiveModuleJump.IDENTIFIER}, new ItemRarmorModule[]{itemModuleSpeed, itemModuleJump});

        itemModuleProtectionIron = new ItemModuleProtection("protection_iron", ActiveModuleProtectionIron.IDENTIFIER);
        itemModuleProtectionGold = new ItemModuleProtection("protection_gold", ActiveModuleProtectionGold.IDENTIFIER);
        itemModuleProtectionDiamond = new ItemModuleProtection("protection_diamond", ActiveModuleProtectionDiamond.IDENTIFIER);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient(){
        ClientProxy.addLocation(itemRarmorHelmet);
        ClientProxy.addLocation(itemRarmorChest, 0);
        ClientProxy.addLocation(itemRarmorChest, 1);
        ClientProxy.addLocation(itemRarmorPants);
        ClientProxy.addLocation(itemRarmorBoots);

        ClientProxy.addLocation(itemWireCutter);
        ClientProxy.addLocation(itemControlCircuit);
        ClientProxy.addLocation(itemWire);
        ClientProxy.addLocation(itemConnector);
        ClientProxy.addLocation(itemSolarCell);
        ClientProxy.addLocation(itemGenerator);
        ClientProxy.addLocation(itemBattery);

        ClientProxy.addLocation(itemModuleStorage);
        ClientProxy.addLocation(itemModuleEnder);
        ClientProxy.addLocation(itemModuleFurnace);
        ClientProxy.addLocation(itemModuleSolar);
        ClientProxy.addLocation(itemModuleGenerator);
        ClientProxy.addLocation(itemModuleSpeed);
        ClientProxy.addLocation(itemModuleJump);
        ClientProxy.addLocation(itemModuleMovement);

        ClientProxy.addLocation(itemModuleProtectionIron);
        ClientProxy.addLocation(itemModuleProtectionGold);
        ClientProxy.addLocation(itemModuleProtectionDiamond);
    }

}
