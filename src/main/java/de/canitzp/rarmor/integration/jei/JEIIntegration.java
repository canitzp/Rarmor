package de.canitzp.rarmor.integration.jei;

import de.canitzp.rarmor.items.ItemRegistry;
import mezz.jei.api.*;

/**
 * @author canitzp
 */
@JEIPlugin
public class JEIIntegration implements IModPlugin {
    private IJeiHelpers helpers;

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers iJeiHelpers) {
        this.helpers = iJeiHelpers;
    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry iItemRegistry) {

    }

    @Override
    public void register(IModRegistry iModRegistry) {
        INbtIgnoreList ignoreList = this.helpers.getNbtIgnoreList();
        ignoreList.ignoreNbtTagNames(ItemRegistry.ironChainsaw, "Energy");
        ignoreList.ignoreNbtTagNames(ItemRegistry.diamondChainsaw, "Energy");
        ignoreList.ignoreNbtTagNames(ItemRegistry.rfArmorBody, "Energy", "isFirstOpened", "rfPerTick");
        ignoreList.ignoreNbtTagNames(ItemRegistry.rfArmorBoots, "Energy", "isFirstOpened");
        ignoreList.ignoreNbtTagNames(ItemRegistry.rfArmorHelmet, "Energy", "isFirstOpened");
        ignoreList.ignoreNbtTagNames(ItemRegistry.rfArmorLeggins, "Energy", "isFirstOpened");
    }

    /**
     * Called when the IRecipeRegistry is available, after all mods have registered.
     *
     * @param recipeRegistry
     * @deprecated since JEI 2.23.0. Get the recipe registry from jeiRuntime, passed in onRuntimeAvailable
     */
    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

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
