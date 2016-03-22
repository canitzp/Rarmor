package de.canitzp.rarmor.integration.jei;

import de.canitzp.rarmor.items.ItemRegistry;
import mezz.jei.api.*;

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
