package de.canitzp.rarmor.integration;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

/**
 * @author canitzp
 */
public class Integration {

    public static final String TESLA = "Tesla";
    public static final String ACTADD = "actuallyadditions";

    public static void init(FMLPostInitializationEvent event){
        if(Loader.isModLoaded(TESLA)){
            RarmorAPI.registerInWorldTooltip(new Tesla());
            Rarmor.logger.info("Loaded Tesla integration.");
        }
        if(Loader.isModLoaded(ACTADD)){
            ActuallyAdditions.init();
            Rarmor.logger.info("Loaded ActuallyAdditions integration.");
        }
    }

}
