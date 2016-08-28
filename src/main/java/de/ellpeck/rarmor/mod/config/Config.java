/*
 * This file ("Config.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.config;

import de.ellpeck.rarmor.api.RarmorAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public final class Config{

    public static Configuration config;

    public static boolean doOpeningConfirmationPacket;
    public static boolean doUpdateCheck;
    public static int rarmorOverlayX;
    public static int rarmorOverlayY;
    public static float rarmorOverlayScale;

    public Config(File file){
        config = new Configuration(file);
        config.load();
        this.config();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void config(){
        doOpeningConfirmationPacket = config.get(Configuration.CATEGORY_GENERAL, "openingConfirmation", true, "Turn this off to disable the packet that gets sent from the client back to the server to ensure that it has gotten all of the data a Rarmor contains before opening its GUI. Turning this off might reduce server load, but could cause bugs. Use at your own risk.").getBoolean();
        doUpdateCheck = config.get(Configuration.CATEGORY_GENERAL, "updateCheck", true, "Turn this off to disable the Update Checker").getBoolean();

        rarmorOverlayX = config.get(Configuration.CATEGORY_GENERAL, "overlayX", 3, "The X position of the Rarmor overlay").getInt();
        rarmorOverlayY = config.get(Configuration.CATEGORY_GENERAL, "overlayY", 3, "The Y position of the Rarmor overlay").getInt();
        rarmorOverlayScale = (float)config.get(Configuration.CATEGORY_GENERAL, "overlayScale", 1.0, "The scale of the Rarmor overlay").getDouble();

        if(config.hasChanged()){
            config.save();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event){
        if(RarmorAPI.MOD_ID.equalsIgnoreCase(event.getModID())){
            this.config();
        }
    }
}
