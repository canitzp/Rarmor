package de.canitzp.rarmor.integration.craftingTweaks;

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
        if (isActive){
            SimpleTweakProvider providerRarmor = CraftingTweaksAPI.registerSimpleProvider(Rarmor.MODID, ContainerRFArmor.class);
            providerRarmor.setGrid(64, 9);
            providerRarmor.setTweakRotate(true, true, 235, 14);
            providerRarmor.setTweakBalance(true, true, 235, 32);
            providerRarmor.setTweakClear(true, true, 235, 50);
        }
    }


}
