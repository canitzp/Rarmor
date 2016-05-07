/*
 * This file 'JEIIntegration.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.integration;

import de.canitzp.rarmor.inventory.container.ContainerRFArmor;
import de.canitzp.rarmor.items.ItemRegistry;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
@SuppressWarnings("ALL")
@JEIPlugin
public class JEIIntegration implements IModPlugin{

    @Override
    public void register(IModRegistry iModRegistry){
        iModRegistry.getJeiHelpers().getNbtIgnoreList().ignoreNbtTagNames("Energy", "isFirstOpened", "rfPerTick");
        iModRegistry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerRFArmor.class, VanillaRecipeCategoryUid.CRAFTING, 64, 9, 27, 36);
        iModRegistry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerRFArmor.class, VanillaRecipeCategoryUid.SMELTING, 73, 1, 27, 36);
        iModRegistry.addRecipeCategoryCraftingItem(new ItemStack(ItemRegistry.rarmorChestplate), VanillaRecipeCategoryUid.CRAFTING, VanillaRecipeCategoryUid.SMELTING);
    }

    /**
     * Called when jei's runtime features are available, after all mods have registered.
     *
     * @param jeiRuntime
     * @since JEI 2.23.0
     */
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime){
    }
}
