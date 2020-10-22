/*
 * This file ("ItemRegistry.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import de.canitzp.rarmor.api.RarmorAPI;
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
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public final class ItemRegistry{
    
    public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, RarmorAPI.MOD_ID);

    public static RegistryObject<ItemRarmor> itemRarmorHelmet = ITEM_REGISTRY.register("item_rarmor_helmet", () -> new ItemRarmor(EquipmentSlotType.HEAD));
    public static RegistryObject<ItemRarmorChest> itemRarmorChest = ITEM_REGISTRY.register("item_rarmor_chest", ItemRarmorChest::new);
    public static RegistryObject<ItemRarmor> itemRarmorPants = ITEM_REGISTRY.register("item_rarmor_pants", () -> new ItemRarmor(EquipmentSlotType.LEGS));
    public static RegistryObject<ItemRarmor> itemRarmorBoots = ITEM_REGISTRY.register("item_rarmor_boots", () -> new ItemRarmor(EquipmentSlotType.FEET));

    public static RegistryObject<ItemWireCutter> itemWireCutter = ITEM_REGISTRY.register("item_wire_cutter", ItemWireCutter::new);
    public static RegistryObject<ItemBase> itemControlCircuit = ITEM_REGISTRY.register("item_control_circuit", ItemBase::new);
    public static RegistryObject<ItemBase> itemWire = ITEM_REGISTRY.register("item_wire", ItemBase::new);
    public static RegistryObject<ItemBase> itemConnector = ITEM_REGISTRY.register("item_connector", ItemBase::new);
    public static RegistryObject<ItemBase> itemSolarCell = ITEM_REGISTRY.register("item_solar_cell", ItemBase::new);
    public static RegistryObject<ItemBase> itemGenerator = ITEM_REGISTRY.register("item_generator", ItemBase::new);
    public static RegistryObject<ItemBattery> itemBattery = ITEM_REGISTRY.register("item_battery", ItemBattery::new);

    public static RegistryObject<ItemModuleStorage> itemModuleStorage = ITEM_REGISTRY.register("item_module_storage", ItemModuleStorage::new);
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
