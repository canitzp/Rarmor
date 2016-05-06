/*
 * This file 'CraftingTweaksIntegration.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.integration;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.inventory.container.ContainerRFArmor;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
import net.blay09.mods.craftingtweaks.api.SimpleTweakProvider;
import net.minecraftforge.fml.common.Loader;

/**
 * @author canitzp
 */
public class CraftingTweaksIntegration{

    public static boolean isActive = false;

    public static void init(){
        isActive = Loader.isModLoaded("craftingtweaks");
        if(isActive){
            SimpleTweakProvider providerRarmor = CraftingTweaksAPI.registerSimpleProvider(Rarmor.MODID, ContainerRFArmor.class);
            providerRarmor.setGrid(64, 9);
            providerRarmor.setTweakRotate(true, true, 235, 14);
            providerRarmor.setTweakBalance(true, true, 235, 32);
            providerRarmor.setTweakClear(true, true, 235, 50);
        }
    }


}
