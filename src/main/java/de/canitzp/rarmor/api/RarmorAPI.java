/*
 * This file ("RarmorAPI.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.api;

import de.canitzp.rarmor.api.module.IActiveRarmorModule;

import java.util.HashMap;
import java.util.Map;

public final class RarmorAPI{

    public static final String MOD_ID = "rarmor";
    public static final String API_NAME = MOD_ID+"api";
    public static final String API_VERSION = "1";

    public static final Map<String, Class<? extends IActiveRarmorModule>> RARMOR_MODULE_REGISTRY = new HashMap<String, Class<? extends IActiveRarmorModule>>();

    public static void registerRarmorModule(String id, Class<? extends IActiveRarmorModule> moduleClass){
        RARMOR_MODULE_REGISTRY.put(id, moduleClass);
    }

}
