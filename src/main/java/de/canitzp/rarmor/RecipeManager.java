package de.canitzp.rarmor;

import de.canitzp.rarmor.items.ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.rfArmorHelmet), "DAD", "ICI", "R R", 'A', ItemRegistry.advancedEyeMatrix, 'C', ItemRegistry.electricalController, 'D', "gemDiamond", 'I', "ingotIron", 'R', ItemRegistry.ribbonCable));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.ironChainsaw), "I  ", "II ", "CRI", 'C', ItemRegistry.electricalController, 'I', "ingotIron", 'R', ItemRegistry.ribbonCable));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.diamondChainsaw), "I  ", "II ", "CRI", 'C', ItemRegistry.electricalController, 'I', "gemDiamond", 'R', ItemRegistry.ribbonCable));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.moduleGenerator), " C ", "RFR", "R R", 'C', ItemRegistry.electricalController, 'F', Blocks.furnace, 'R', ItemRegistry.ribbonCable));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.advancedEyeMatrix), "GIG", "RGB", "DED", 'D', "gemDiamond", 'E', "dyeGreen", 'B', "dyeBlue", 'G', "blockGlass", 'R', "dyeRed", 'I', "ingotIron"));
        addRecipe(new ItemStack(ItemRegistry.moduleFastFurnace), "GIG", "GDG", "GFG", 'G', "dyeGreen", 'I', "ingotIron", 'F', Blocks.furnace, 'D', "gemDiamond");
        addRecipe(new ItemStack(ItemRegistry.moduleSolarPanel), "GDG", "GCG", "GDG", 'G', "dyeGreen", 'C', Items.coal, 'D', "gemDiamond");
        addRecipe(new ItemStack(ItemRegistry.moduleFlying), "GDG", "GNG", "GDG", 'G', "dyeGreen", 'N', Items.nether_star, 'D', "gemDiamond");
    }

    public static void addRecipe(ItemStack output, Object... input){
        GameRegistry.addRecipe(new ShapedOreRecipe(output, input));
    }

}
