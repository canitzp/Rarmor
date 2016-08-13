/*
 * This file ("Helper.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.misc;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.module.IActiveRarmorModule;
import de.canitzp.rarmor.mod.Rarmor;

import java.util.Map;

public final class Helper{

    public static IActiveRarmorModule initiateModuleById(String id){
        if(id != null && !id.isEmpty()){
            Class<? extends IActiveRarmorModule> moduleClass = RarmorAPI.RARMOR_MODULE_REGISTRY.get(id);
            return initiateModule(moduleClass);
        }
        return null;
    }

    public static IActiveRarmorModule initiateModule(Class<? extends IActiveRarmorModule> moduleClass){
        try{
            return moduleClass.newInstance();
        }
        catch(Exception e){
            Rarmor.LOGGER.error("Trying to initiate a module failed!", e);
            return null;
        }
    }

    public static String getIdFromModule(IActiveRarmorModule module){
        if(module != null){
            Class<? extends IActiveRarmorModule> moduleClass = module.getClass();
            for(Map.Entry<String, Class<? extends IActiveRarmorModule>> entry : RarmorAPI.RARMOR_MODULE_REGISTRY.entrySet()){
                if(moduleClass.equals(entry.getValue())){
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}
