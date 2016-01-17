package de.canitzp.rarmor;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import de.canitzp.rarmor.items.ItemRegistry;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @author canitzp
 */
public class RecipeManager {

    public static void init(){
        loadVanilla();
    }

    private static void loadActAddRecipes() {
        //ActuallyAdditionsAPI.addReconstructorLensNoneRecipe();
    }

    public static void loadVanilla(){
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.ribbonCable, 12), "WIW", "WIW", "WIW", 'W', Blocks.wool, 'I', "nuggetGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.electricalController, 1), " G ", "GIG", "CGC", 'C', ItemRegistry.ribbonCable, 'G', "dyeGreen", 'I', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.rfArmorBoots), "   ", "DCD", "IRI", 'C', ItemRegistry.electricalController, 'D', "gemDiamond", 'I', "ingotIron", 'R', ItemRegistry.ribbonCable));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.rfArmorLeggins), "DCD", "IRI", "I I", 'C', ItemRegistry.electricalController, 'D', "gemDiamond", 'I', "ingotIron", 'R', ItemRegistry.ribbonCable));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.rfArmorBody), "DDD", "CDC", "IRI", 'C', ItemRegistry.electricalController, 'D', "gemDiamond", 'I', "ingotIron", 'R', ItemRegistry.ribbonCable));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.rfArmorHelmet), "DRD", "ICI", "   ", 'C', ItemRegistry.electricalController, 'D', "gemDiamond", 'I', "ingotIron", 'R', ItemRegistry.ribbonCable));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.ironChainsaw), "I  ", "II ", "CRI", 'C', ItemRegistry.electricalController, 'I', "ingotIron", 'R', ItemRegistry.ribbonCable));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.diamondChainsaw), "I  ", "II ", "CRI", 'C', ItemRegistry.electricalController, 'I', "gemDiamond", 'R', ItemRegistry.ribbonCable));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.moduleGenerator), " C ", "RFR", "R R", 'C', ItemRegistry.electricalController, 'F', Blocks.furnace, 'R', ItemRegistry.ribbonCable));
    }

}
