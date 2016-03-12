package de.canitzp.rarmor.event;

import net.minecraftforge.common.MinecraftForge;

/**
 * @author canitzp
 */
public class EventHandler {

    public static void init(){
        GuiOpenEvent guiChanger = new GuiOpenEvent();
        GameOverlayEvent hud = new GameOverlayEvent();
        PlayerEvents joinPlayer = new PlayerEvents();
        MinecraftForge.EVENT_BUS.register(guiChanger);
        MinecraftForge.EVENT_BUS.register(hud);
        MinecraftForge.EVENT_BUS.register(joinPlayer);
    }
}
