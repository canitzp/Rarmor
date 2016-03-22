package de.canitzp.rarmor.api;

import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public enum RarmorResources {

    GUIELEMENTS("guiGuiElements"),
    RARMORGUI("guiRFArmorNormal"),
    RARMORMODULEGUI("guiRFArmorModules"),
    BATTERYGUI("guiBattery"),
    MODULARTOOLGUI("guiModularTool");

    public String textureName;
    RarmorResources(String location) {
        this.textureName = location;
    }
    public ResourceLocation getNewLocation() {
        return new ResourceLocation("rarmor:textures/gui/" + textureName + ".png");
    }

}
