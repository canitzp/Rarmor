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
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
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
        itemRarmorHelmet = new ItemRarmor("item_rarmor_helmet", EquipmentSlotType.HEAD);
        itemRarmorChest = new ItemRarmorChest("item_rarmor_chest");
        itemRarmorPants = new ItemRarmor("item_rarmor_pants", EquipmentSlotType.LEGS);
        itemRarmorBoots = new ItemRarmor("item_rarmor_boots", EquipmentSlotType.FEET);

        itemWireCutter = new ItemWireCutter("item_wire_cutter");
        itemControlCircuit = new ItemBase("item_control_circuit", new Item.Properties()).addOreDict("circuitBasic");
        itemWire = new ItemBase("item_wire", new Item.Properties());
        itemConnector = new ItemBase("item_connector", new Item.Properties());
        itemSolarCell = new ItemBase("item_solar_cell", new Item.Properties());
        itemGenerator = new ItemBase("item_generator", new Item.Properties());
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

    @SubscribeEvent
    public static void registerItemsEvent(RegistryEvent.Register<Item> event){
        // tod register Items
    }
    
}
