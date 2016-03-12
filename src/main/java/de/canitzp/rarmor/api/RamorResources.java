package de.canitzp.rarmor.api;

import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public enum RamorResources {

    CIRCUITCREATOREMPTYGUI("guiCircuitCreator"),
    CIRCUITCREATORGUI("guiCircuitCreatorGrid"),
    ELECTRICALCOMPONENTS("guiElectricalComponents"),
    GUIELEMENTS("guiGuiElements"),
    RARMORGUI("guiRFArmorNormal"),
    RARMORMODULEGUI("guiRFArmorModules"),
    BATTERYGUI("guiBattery");

    public String textureName;
    RamorResources(String location) {
        this.textureName = location;
    }
    public ResourceLocation getNewLocation() {
        return new ResourceLocation("rarmor:textures/gui/" + textureName + ".png");
    }

}
