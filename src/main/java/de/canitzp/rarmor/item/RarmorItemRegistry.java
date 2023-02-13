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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class RarmorItemRegistry {
    
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, RarmorAPI.MOD_ID);

    public static RegistryObject<ItemRarmor> itemRarmorHelmet = REGISTRY.register("item_rarmor_helmet", () -> new ItemRarmor(EquipmentSlot.HEAD));
    public static RegistryObject<ItemRarmorChest> itemRarmorChest = REGISTRY.register("item_rarmor_chest", ItemRarmorChest::new);
    public static RegistryObject<ItemRarmor> itemRarmorPants = REGISTRY.register("item_rarmor_pants", () -> new ItemRarmor(EquipmentSlot.LEGS));
    public static RegistryObject<ItemRarmor> itemRarmorBoots = REGISTRY.register("item_rarmor_boots", () -> new ItemRarmor(EquipmentSlot.FEET));

    public static RegistryObject<ItemWireCutter> itemWireCutter = REGISTRY.register("item_wire_cutter", ItemWireCutter::new);
    public static RegistryObject<ItemBase> itemControlCircuit = REGISTRY.register("item_control_circuit", ItemBase::new);
    public static RegistryObject<ItemBase> itemWire = REGISTRY.register("item_wire", ItemBase::new);
    public static RegistryObject<ItemBase> itemConnector = REGISTRY.register("item_connector", ItemBase::new);
    public static RegistryObject<ItemBase> itemSolarCell = REGISTRY.register("item_solar_cell", ItemBase::new);
    public static RegistryObject<ItemBase> itemGenerator = REGISTRY.register("item_generator", ItemBase::new);
    public static RegistryObject<ItemBattery> itemBattery = REGISTRY.register("item_battery", ItemBattery::new);

    public static RegistryObject<ItemModuleStorage> itemModuleStorage = REGISTRY.register("item_module_storage", ItemModuleStorage::new);
    public static RegistryObject<ItemModuleEnder> itemModuleEnder = REGISTRY.register("item_module_ender", ItemModuleEnder::new);
    public static RegistryObject<ItemModuleFurnace> itemModuleFurnace = REGISTRY.register("item_module_furnace", ItemModuleFurnace::new);
    public static RegistryObject<ItemModuleSolar> itemModuleSolar = REGISTRY.register("item_module_solar", ItemModuleSolar::new);
    public static RegistryObject<ItemModuleGenerator> itemModuleGenerator = REGISTRY.register("item_module_generator", ItemModuleGenerator::new);
    public static RegistryObject<ItemModuleSpeed> itemModuleSpeed = REGISTRY.register("item_module_speed", ItemModuleSpeed::new);
    public static RegistryObject<ItemModuleJump> itemModuleJump = REGISTRY.register("item_module_jump", ItemModuleJump::new);
    public static RegistryObject<ItemModuleCompound> itemModuleMovement = REGISTRY.register("item_module_movement", () -> new ItemModuleCompound(new String[]{ActiveModuleSpeed.IDENTIFIER, ActiveModuleJump.IDENTIFIER}, new ItemRarmorModule[]{itemModuleSpeed.get(), itemModuleJump.get()}));

    public static RegistryObject<ItemModuleProtection> itemModuleProtectionIron = REGISTRY.register("item_module_protection_iron", () -> new ItemModuleProtection(ActiveModuleProtectionIron.IDENTIFIER));
    public static RegistryObject<ItemModuleProtection> itemModuleProtectionGold = REGISTRY.register("item_module_protection_gold", () -> new ItemModuleProtection(ActiveModuleProtectionGold.IDENTIFIER));
    public static RegistryObject<ItemModuleProtection> itemModuleProtectionDiamond = REGISTRY.register("item_module_protection_diamond", () -> new ItemModuleProtection(ActiveModuleProtectionDiamond.IDENTIFIER));

}
