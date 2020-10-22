/*
 * This file ("RarmorAPI.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.api;

import de.canitzp.rarmor.api.internal.IMethodHandler;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;

import java.util.HashMap;
import java.util.Map;

public final class RarmorAPI{

    public static final String MOD_ID = "rarmor";
    public static final String API_NAME = MOD_ID+"api";
    public static final String API_VERSION = "1";

    public static final Map<String, Class<? extends ActiveRarmorModule>> RARMOR_MODULE_REGISTRY = new HashMap<String, Class<? extends ActiveRarmorModule>>();

    /**
     * The internal method handler.
     * This can be used to access some methods that might be useful when developing add-ons.
     * <p>
     * Do not overwrite this or a kitten will die.
     */
    public static IMethodHandler methodHandler;

    /**
     * Registers a Rarmor module
     *
     * @param id          The unique id this module should have.
     *                    Please make sure to somehow have the mod's id or name be part of this to reduce the risk of it not being unique.
     * @param moduleClass The class the module is contained in
     */
    public static void registerRarmorModule(String id, Class<? extends ActiveRarmorModule> moduleClass){
        RARMOR_MODULE_REGISTRY.put(id, moduleClass);
    }
}
