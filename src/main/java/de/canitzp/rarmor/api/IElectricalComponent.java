package de.canitzp.rarmor.api;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author canitzp
 */
public interface IElectricalComponent {

    ResourceLocation guiComponent();

    int getWidth();

    int getHeight();

    int getTextureWidth();

    int getTextureHeight();

    int getTextureX();

    int getTextureY();

    List<String> getHoveringText();

    int getX();

    int getY();

    /**
     * Ohm
     */
    default int resistance(){
        return 0;
    }

    /**
     * Farad
     */
    default int capacitance(){
        return 0;
    }

    /**
     * Henry
     */
    default float inductivity(){
        return 0;
    }

    /**
     * Volt
     */
    default int maxVoltage(){
        return 0;
    }

    /**
     * Ampere
     */
    default float maxCurrent(){
        return 0;
    }

    /**
     * Watts
     */
    default float maxPower(){
        return 0.1F;
    }

    default void render(GuiScreen gui, FontRenderer renderer){}

    default NBTTagCompound writeNBT(NBTTagCompound compound, GuiSetting setting){return compound;}

    void onGuiSettingsClosed(GuiSetting setting);

}
