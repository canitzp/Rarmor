/*
 * This file 'EventHandler.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.event;

import de.canitzp.rarmor.Rarmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * @author canitzp
 */
public class EventHandler{

    public static void init(FMLInitializationEvent event){
        Rarmor.logger.info("Register Events");
        GuiOpenEvent guiChanger = new GuiOpenEvent();
        PlayerEvents joinPlayer = new PlayerEvents();
        MinecraftForge.EVENT_BUS.register(guiChanger);
        MinecraftForge.EVENT_BUS.register(joinPlayer);
        if(event.getSide().isClient()){
            ClientEvents client = new ClientEvents();
            MinecraftForge.EVENT_BUS.register(client);
        }
    }

}
