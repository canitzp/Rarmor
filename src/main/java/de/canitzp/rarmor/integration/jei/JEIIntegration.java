package de.canitzp.rarmor.integration.jei;

import de.canitzp.rarmor.inventory.container.ContainerRFArmor;
import de.canitzp.rarmor.items.ItemRegistry;
import mezz.jei.api.*;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

/**
 * @author canitzp
 */
@JEIPlugin
public class JEIIntegration implements IModPlugin {

    @Override
    public void register(IModRegistry iModRegistry) {
        INbtIgnoreList ignoreList = iModRegistry.getJeiHelpers().getNbtIgnoreList();
        ignoreList.ignoreNbtTagNames(ItemRegistry.ironChainsaw, "Energy");
        ignoreList.ignoreNbtTagNames(ItemRegistry.diamondChainsaw, "Energy");
        ignoreList.ignoreNbtTagNames(ItemRegistry.rfArmorBody, "Energy", "isFirstOpened", "rfPerTick");
        ignoreList.ignoreNbtTagNames(ItemRegistry.rfArmorBoots, "Energy", "isFirstOpened");
        ignoreList.ignoreNbtTagNames(ItemRegistry.rfArmorHelmet, "Energy", "isFirstOpened");
        ignoreList.ignoreNbtTagNames(ItemRegistry.rfArmorLeggins, "Energy", "isFirstOpened");
        iModRegistry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerRFArmor.class, VanillaRecipeCategoryUid.CRAFTING, 64, 9, 27, 36);
        iModRegistry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerRFArmor.class, VanillaRecipeCategoryUid.SMELTING, 73, 1, 27, 36);
    }

    /**
     * Called when jei's runtime features are available, after all mods have registered.
     *
     * @param jeiRuntime
     * @since JEI 2.23.0
     */
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    }
}
