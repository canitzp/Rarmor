package de.canitzp.rarmor.event;

import net.minecraftforge.common.MinecraftForge;

/**
 * @author canitzp
 */
public class EventHandler {

    public static void init(){
        GuiOpenEvent guiChanger = new GuiOpenEvent();
        GameOverlayEvent hud = new GameOverlayEvent();
        PlayerJoinEvent joinPlayer = new PlayerJoinEvent();
        MinecraftForge.EVENT_BUS.register(guiChanger);
        MinecraftForge.EVENT_BUS.register(hud);
        MinecraftForge.EVENT_BUS.register(joinPlayer);
    }
}
