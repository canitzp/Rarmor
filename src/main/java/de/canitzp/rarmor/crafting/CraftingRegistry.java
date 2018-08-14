/*
 * This file ("CraftingRegistry.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.crafting;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.item.ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = RarmorAPI.MOD_ID)
public final class CraftingRegistry{

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> evt){
        IForgeRegistry<IRecipe> reg = evt.getRegistry();
        addModuleRecipe(reg, ItemRegistry.itemModuleStorage, "chestWood");
        addModuleRecipe(reg, ItemRegistry.itemModuleEnder, "chestEnder");
        addModuleRecipe(reg, ItemRegistry.itemModuleFurnace, new ItemStack(Blocks.FURNACE));
        addModuleRecipe(reg, ItemRegistry.itemModuleSolar, new ItemStack(ItemRegistry.itemSolarCell));
        addModuleRecipe(reg, ItemRegistry.itemModuleGenerator, new ItemStack(ItemRegistry.itemGenerator));
        addModuleRecipe(reg, ItemRegistry.itemModuleSpeed, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_SWIFTNESS));
        addModuleRecipe(reg, ItemRegistry.itemModuleJump, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_LEAPING));
        addModuleRecipe(reg, ItemRegistry.itemModuleProtectionIron, "blockIron");
        addModuleRecipe(reg, ItemRegistry.itemModuleProtectionGold, "blockGold");
        addModuleRecipe(reg, ItemRegistry.itemModuleProtectionDiamond, "blockDiamond");

        reg.register(new ShapedOreRecipe(null, new ItemStack(ItemRegistry.itemModuleMovement),
            " W ", "SCJ", " W ",
            'S', ItemRegistry.itemModuleSpeed,
            'J', ItemRegistry.itemModuleJump,
            'C', "circuitBasic",
            'W', ItemRegistry.itemWire).setRegistryName(RarmorAPI.MOD_ID, "recipe_item_module_movement"));
    
        reg.register(new ShapedOreRecipe(null, new ItemStack(ItemRegistry.itemGenerator),
            "IWI", "IFI", "IWI",
            'I', "ingotIron",
            'F', Blocks.FURNACE,
            'W', ItemRegistry.itemWire).setRegistryName(RarmorAPI.MOD_ID, "recipe_item_generator"));
    
        reg.register(new ShapedOreRecipe(null, new ItemStack(ItemRegistry.itemSolarCell),
            "LLL", "WDW",
            'L', new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()),
            'W', ItemRegistry.itemWire,
            'D', Blocks.DAYLIGHT_DETECTOR).setRegistryName(RarmorAPI.MOD_ID, "recipe_item_solar_cell"));
    
        reg.register(new ShapedOreRecipe(null, new ItemStack(ItemRegistry.itemRarmorHelmet),
            " W ", "CDC", " W ",
            'D', Items.DIAMOND_HELMET,
            'C', "circuitBasic",
            'W', ItemRegistry.itemWire).setRegistryName(RarmorAPI.MOD_ID, "recipe_item_rarmor_helmet"));
    
        reg.register(new ShapedOreRecipe(null, new ItemStack(ItemRegistry.itemRarmorChest),
            "WBW", "CDC", "WBW",
            'D', Items.DIAMOND_CHESTPLATE,
            'B', ItemRegistry.itemBattery,
            'C', "circuitBasic",
            'W', ItemRegistry.itemWire).setRegistryName(RarmorAPI.MOD_ID, "recipe_item_rarmor_chest"));
    
        reg.register(new ShapedOreRecipe(null, new ItemStack(ItemRegistry.itemRarmorPants),
            " W ", "CDC", " W ",
            'D', Items.DIAMOND_LEGGINGS,
            'C', "circuitBasic",
            'W', ItemRegistry.itemWire).setRegistryName(RarmorAPI.MOD_ID, "recipe_item_rarmor_pants"));
    
        reg.register(new ShapedOreRecipe(null, new ItemStack(ItemRegistry.itemRarmorBoots),
            " W ", "CDC", " W ",
            'D', Items.DIAMOND_BOOTS,
            'C', "circuitBasic",
            'W', ItemRegistry.itemWire).setRegistryName(RarmorAPI.MOD_ID, "recipe_item_rarmor_boots"));
    
        reg.register(new ShapedOreRecipe(null, new ItemStack(ItemRegistry.itemBattery),
            " R ", "CWC", "OWO",
            'R', "dustRedstone",
            'C', "circuitBasic",
            'O', ItemRegistry.itemConnector,
            'W', ItemRegistry.itemWire).setRegistryName(RarmorAPI.MOD_ID, "recipe_item_battery"));
    
        reg.register(new ShapelessOreRecipe(null, new ItemStack(ItemRegistry.itemConnector),
            new ItemStack(ItemRegistry.itemWireCutter, 1, OreDictionary.WILDCARD_VALUE),
            ItemRegistry.itemWire,
            "dustRedstone",
            "ingotIron").setRegistryName(RarmorAPI.MOD_ID, "recipe_item_connector"));
    
        reg.register(new ShapedOreRecipe(null, new ItemStack(ItemRegistry.itemControlCircuit),
            "RWR", "WCW", "RWR",
            'W', ItemRegistry.itemWire,
            'C', ItemRegistry.itemConnector,
            'R', "dustRedstone").setRegistryName(RarmorAPI.MOD_ID, "recipe_item_control_circuit"));
    
        reg.register(new ShapedOreRecipe(null, new ItemStack(ItemRegistry.itemWireCutter),
            "F F", " I ", "IRI",
            'I', "ingotIron",
            'R', "dyeRed",
            'F', Items.FLINT).setRegistryName(RarmorAPI.MOD_ID, "recipe_item_wire_cutter"));
    
        reg.register(new ShapelessOreRecipe(null, new ItemStack(ItemRegistry.itemWire),
            new ItemStack(ItemRegistry.itemWireCutter, 1, OreDictionary.WILDCARD_VALUE),
            "dustRedstone").setRegistryName(RarmorAPI.MOD_ID, "recipe_item_wire"));
    }

    private static void addModuleRecipe(IForgeRegistry<IRecipe> reg, Item output, Object input){
        reg.register(new ShapedOreRecipe(null, new ItemStack(output),
            "OWO", "CXC", "OWO",
            'C', "circuitBasic",
            'W', ItemRegistry.itemWire,
            'O', ItemRegistry.itemConnector,
            'X', input).setRegistryName(RarmorAPI.MOD_ID, "recipe_" + output.getRegistryName().getPath()));
    }
}
