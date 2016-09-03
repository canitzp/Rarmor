/*
 * This file ("CraftingRegistry.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.crafting;

import de.ellpeck.rarmor.mod.item.ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public final class CraftingRegistry{

    public static void init(){
        addModuleRecipe(ItemRegistry.itemModuleStorage, new ItemStack(Blocks.CHEST));
        addModuleRecipe(ItemRegistry.itemModuleEnder, new ItemStack(Blocks.ENDER_CHEST));
        addModuleRecipe(ItemRegistry.itemModuleFurnace, new ItemStack(Blocks.FURNACE));
        addModuleRecipe(ItemRegistry.itemModuleSolar, new ItemStack(ItemRegistry.itemSolarCell));
        addModuleRecipe(ItemRegistry.itemModuleGenerator, new ItemStack(ItemRegistry.itemGenerator));
        addModuleRecipe(ItemRegistry.itemModuleSpeed, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_SWIFTNESS));
        addModuleRecipe(ItemRegistry.itemModuleJump, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_LEAPING));

        GameRegistry.addRecipe(new ItemStack(ItemRegistry.itemModuleMovement),
                " W ", "SCJ", " W ",
                'S', ItemRegistry.itemModuleSpeed,
                'J', ItemRegistry.itemModuleJump,
                'C', ItemRegistry.itemControlCircuit,
                'W', ItemRegistry.itemWire);

        GameRegistry.addRecipe(new ItemStack(ItemRegistry.itemGenerator),
                "IWI", "IFI", "IWI",
                'I', Items.IRON_INGOT,
                'F', Blocks.FURNACE,
                'W', ItemRegistry.itemWire);

        GameRegistry.addRecipe(new ItemStack(ItemRegistry.itemSolarCell),
                "LLL", "WDW",
                'L', new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()),
                'W', ItemRegistry.itemWire,
                'D', Blocks.DAYLIGHT_DETECTOR);

        GameRegistry.addRecipe(new ItemStack(ItemRegistry.itemRarmorHelmet),
                " W ", "CDC", " W ",
                'D', Items.DIAMOND_HELMET,
                'C', ItemRegistry.itemControlCircuit,
                'W', ItemRegistry.itemWire);

        GameRegistry.addRecipe(new ItemStack(ItemRegistry.itemRarmorChest),
                "WBW", "CDC", "WBW",
                'D', Items.DIAMOND_CHESTPLATE,
                'B', ItemRegistry.itemBattery,
                'C', ItemRegistry.itemControlCircuit,
                'W', ItemRegistry.itemWire);

        GameRegistry.addRecipe(new ItemStack(ItemRegistry.itemRarmorPants),
                " W ", "CDC", " W ",
                'D', Items.DIAMOND_LEGGINGS,
                'C', ItemRegistry.itemControlCircuit,
                'W', ItemRegistry.itemWire);

        GameRegistry.addRecipe(new ItemStack(ItemRegistry.itemRarmorBoots),
                " W ", "CDC", " W ",
                'D', Items.DIAMOND_BOOTS,
                'C', ItemRegistry.itemControlCircuit,
                'W', ItemRegistry.itemWire);

        GameRegistry.addRecipe(new ItemStack(ItemRegistry.itemBattery),
                " R ", "CWC", "OWO",
                'R', Items.REDSTONE,
                'C', ItemRegistry.itemControlCircuit,
                'O', ItemRegistry.itemConnector,
                'W', ItemRegistry.itemWire);

        GameRegistry.addShapelessRecipe(new ItemStack(ItemRegistry.itemWire),
                new ItemStack(ItemRegistry.itemWireCutter, 1, OreDictionary.WILDCARD_VALUE),
                Items.REDSTONE);

        GameRegistry.addRecipe(new ItemStack(ItemRegistry.itemControlCircuit),
                "RWR", "WCW", "RWR",
                'W', ItemRegistry.itemWire,
                'C', ItemRegistry.itemConnector,
                'R', Items.REDSTONE);

        GameRegistry.addShapelessRecipe(new ItemStack(ItemRegistry.itemConnector),
                new ItemStack(ItemRegistry.itemWireCutter, 1, OreDictionary.WILDCARD_VALUE),
                ItemRegistry.itemWire,
                Items.REDSTONE,
                Items.IRON_INGOT);

        GameRegistry.addRecipe(new ItemStack(ItemRegistry.itemWireCutter),
                "F F", " I ", "IRI",
                'I', Items.IRON_INGOT,
                'R', new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()),
                'F', Items.FLINT);
    }

    private static void addModuleRecipe(Item output, ItemStack input){
        GameRegistry.addRecipe(new ItemStack(output),
                "OWO", "CXC", "OWO",
                'C', ItemRegistry.itemControlCircuit,
                'W', ItemRegistry.itemWire,
                'O', ItemRegistry.itemConnector,
                'X', input);
    }
}
