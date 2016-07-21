package de.canitzp.rarmor.api;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * @author canitzp
 */
public class RarmorValues {

    /** Standard Mod values: */
    public static final String MODID = "rarmor";
    public static final String MODNAME = "Rarmor";
    public static final String MODVERSION = "@Version@";

    /** Rarmor values: */
    public static int rarmorMaxEnergy;
    public static int rarmorMaxTransfer;

    /** Tab values for Rarmor own tabs: */
    public static int generatorTabTickValue;

    /** InWorld-Tooltips Values */
    public static KeyBinding defaultOpenKey = new KeyBinding("rarmor.key.tooltipsettings", Keyboard.KEY_NUMPAD0, "rarmor.key");
    public static boolean tooltipsAlwaysActive;
    public static float[] defaultValues;


}
