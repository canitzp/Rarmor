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
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.mod.Rarmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class Helper{

    public static ActiveRarmorModule initiateModuleById(String id){
        if(id != null && !id.isEmpty()){
            Class<? extends ActiveRarmorModule> moduleClass = RarmorAPI.RARMOR_MODULE_REGISTRY.get(id);
            ActiveRarmorModule module = initiateModule(moduleClass);

            String moduleId = module.getIdentifier();
            if(!id.equals(moduleId)){
                Rarmor.LOGGER.fatal("A "+Rarmor.MOD_NAME+" Module has a different identifier than the one it was registered with. This is not allowed behavior! Expected id: "+id+", got "+moduleId+".");
            }

            return module;
        }
        return null;
    }

    private static ActiveRarmorModule initiateModule(Class<? extends ActiveRarmorModule> moduleClass){
        try{
            return moduleClass.newInstance();
        }
        catch(Exception e){
            Rarmor.LOGGER.error("Trying to initiate a module failed!", e);
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public static ResourceLocation getGuiLocation(String guiName){
        return new ResourceLocation(RarmorAPI.MOD_ID, "textures/gui/"+guiName+".png");
    }
}
