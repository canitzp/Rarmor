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
    public static boolean rarmorOverlayOnlyEnergy;

    public static int rarmorOpeningMode;
    public static boolean showInventoryButton;

    public Config(File file){
        config = new Configuration(file);
        config.load();
        this.config();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void config(){
        doOpeningConfirmationPacket = config.get(Configuration.CATEGORY_GENERAL, "openingConfirmation", true, "Turn this off to disable the packet that gets sent from the client back to the server to ensure that it has gotten all of the data a Rarmor contains before opening its GUI. Turning this off might reduce server load, but could cause bugs. Use at your own risk.").getBoolean();
        doUpdateCheck = config.get(Configuration.CATEGORY_GENERAL, "updateCheck", true, "Turn this off to disable the Update Checker").getBoolean();

        rarmorOverlayX = config.get(Configuration.CATEGORY_GENERAL, "overlayX", 3, "The X position of the Rarmor overlay. Set this or the y value to a negative number to disable it.").getInt();
        rarmorOverlayY = config.get(Configuration.CATEGORY_GENERAL, "overlayY", 3, "The Y position of the Rarmor overlay. Set this or the x value to a negative number to disable it.").getInt();
        rarmorOverlayScale = (float)config.get(Configuration.CATEGORY_GENERAL, "overlayScale", 1.0, "The scale of the Rarmor overlay").getDouble();
        rarmorOverlayOnlyEnergy = config.get(Configuration.CATEGORY_GENERAL, "overlayOnlyEnergy", false, "If the Rarmor overlay should only show the energy amount").getBoolean();

        rarmorOpeningMode = config.get(Configuration.CATEGORY_GENERAL, "openingMode", 0, "The way the Rarmor GUI can be accessed. 0 is inventory key to open the Rarmor, sneak for normal inventory. 1 is inventory key for normal inventory, sneak to open the Rarmor. 2 is always open the Rarmor, and any other value is never open the Rarmor.").getInt();
        showInventoryButton = config.get(Configuration.CATEGORY_GENERAL, "showButtonInInventory", true, "Show a button to open the Rarmor GUI in the normal inventory").getBoolean();

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
