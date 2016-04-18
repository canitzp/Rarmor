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

    public static void init() {
        Rarmor.logger.info("Register Recipes");
        loadVanilla();
        loadActAddRecipes();
    }

    private static void loadActAddRecipes() {
        //ActuallyAdditionsAPI.addCrusherRecipe(new ItemStack(ItemRegistry.moduleFlying), new ItemStack(Items.nether_star));
    }

    public static void loadVanilla() {
        addRecipe(new ItemStack(ItemRegistry.ribbonCable, 12), "WIW", "WIW", "WIW", 'W', Blocks.WOOL, 'I', "nuggetGold");
        addRecipe(new ItemStack(ItemRegistry.electricalController, 1), " G ", "GIG", "CGC", 'C', ItemRegistry.ribbonCable, 'G', "dyeGreen", 'I', "ingotIron");
        addRecipe(new ItemStack(ItemRegistry.rfArmorBoots), "   ", "DCD", "IRI", 'C', ItemRegistry.electricalController, 'D', "gemDiamond", 'I', "ingotIron", 'R', ItemRegistry.ribbonCable);
        addRecipe(new ItemStack(ItemRegistry.rfArmorLeggins), "DCD", "IRI", "I I", 'C', ItemRegistry.electricalController, 'D', "gemDiamond", 'I', "ingotIron", 'R', ItemRegistry.ribbonCable);
        addRecipe(new ItemStack(ItemRegistry.rfArmorBody), "DDD", "CDC", "IRI", 'C', ItemRegistry.electricalController, 'D', "gemDiamond", 'I', "ingotIron", 'R', ItemRegistry.ribbonCable);
        addRecipe(new ItemStack(ItemRegistry.rfArmorHelmet), "DAD", "ICI", "R R", 'A', ItemRegistry.advancedEyeMatrix, 'C', ItemRegistry.electricalController, 'D', "gemDiamond", 'I', "ingotIron", 'R', ItemRegistry.ribbonCable);
        addRecipe(new ItemStack(ItemRegistry.ironChainsaw), "I  ", "II ", "CRI", 'C', ItemRegistry.electricalController, 'I', "ingotIron", 'R', ItemRegistry.ribbonCable);
        addRecipe(new ItemStack(ItemRegistry.diamondChainsaw), "I  ", "II ", "CRI", 'C', ItemRegistry.electricalController, 'I', "gemDiamond", 'R', ItemRegistry.ribbonCable);
        addRecipe(new ItemStack(ItemRegistry.moduleGenerator), " C ", "RFR", "R R", 'C', ItemRegistry.electricalController, 'F', Blocks.FURNACE, 'R', ItemRegistry.ribbonCable);
        addRecipe(new ItemStack(ItemRegistry.advancedEyeMatrix), "GIG", "RGB", "DED", 'D', "gemDiamond", 'E', "dyeGreen", 'B', "dyeBlue", 'G', "blockGlass", 'R', "dyeRed", 'I', "ingotIron");
        addRecipe(new ItemStack(ItemRegistry.moduleFastFurnace), "GIG", "GDG", "GFG", 'G', "dyeGreen", 'I', "ingotIron", 'F', Blocks.FURNACE, 'D', "gemDiamond");
        addRecipe(new ItemStack(ItemRegistry.moduleSolarPanel), "GDG", "GCG", "GDG", 'G', "dyeGreen", 'C', Items.COAL, 'D', "gemDiamond");
        addRecipe(new ItemStack(ItemRegistry.moduleFlying), "GDG", "GNG", "GDG", 'G', "dyeGreen", 'N', Items.NETHER_STAR, 'D', "gemDiamond");
        addRecipe(new ItemStack(ItemRegistry.moduleLoader), "GIG", "GNG", "GGG", 'G', "dyeGreen", 'N', "nuggetGold", 'I', "ingotIron");
        addRecipe(new ItemStack(ItemRegistry.moduleUnloader), "GGG", "GNG", "GIG", 'G', "dyeGreen", 'N', "nuggetGold", 'I', "ingotIron");
        addRecipe(new ItemStack(ItemRegistry.moduleDefense), "GSG", "GDG", "GGG", 'G', "dyeGreen", 'S', Items.SHIELD, 'D', "gemDiamond");
        addRecipe(new ItemStack(ItemRegistry.moduleModuleSplitter), "GCG", "CDC", "GCG", 'G', "dyeGreen", 'C', ItemRegistry.electricalController, 'D', ItemRegistry.ribbonCable);
    }

    public static void addRecipe(ItemStack output, Object... input) {
        GameRegistry.addRecipe(new ShapedOreRecipe(output, input));
    }

}
