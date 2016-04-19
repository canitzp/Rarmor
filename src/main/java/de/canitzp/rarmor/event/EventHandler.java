package de.canitzp.rarmor.event;

import de.canitzp.rarmor.Rarmor;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author canitzp
 */
public class EventHandler{

    public static void init(){
        Rarmor.logger.info("Register Events");
        GuiOpenEvent guiChanger = new GuiOpenEvent();
        GameOverlayEvent hud = new GameOverlayEvent();
        PlayerEvents joinPlayer = new PlayerEvents();
        MinecraftForge.EVENT_BUS.register(guiChanger);
        MinecraftForge.EVENT_BUS.register(hud);
        MinecraftForge.EVENT_BUS.register(joinPlayer);
    }

    public static void postInitClient(){
        ClientEvents clientEvents = new ClientEvents();
        MinecraftForge.EVENT_BUS.register(clientEvents);
    }
}
