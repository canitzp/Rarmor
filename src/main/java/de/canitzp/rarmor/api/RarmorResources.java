/*
 * This file 'RarmorResources.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.api;

import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public enum RarmorResources{

    GUIELEMENTS("guiGuiElements"),
    RARMORGUI("guiRFArmorNormal"),
    RARMORMODULEGUI("guiRFArmorModules"),
    BATTERYGUI("guiBattery"),
    MODULARTOOLGUI("guiModularTool");

    public String textureName;

    RarmorResources(String location){
        this.textureName = location;
    }

    public ResourceLocation getNewLocation(){
        return new ResourceLocation("rarmor:textures/gui/" + textureName + ".png");
    }

}
