package de.canitzp.rarmor;

/**
 * @author canitzp
 */
public class ElectricalUtil {

    public static float rfToAmpere(int rf){
        return rf / 1000;
    }

    public static int ampereToRF(float ampere){
        return Math.round(ampere * 1000);
    }

    public static float euToAmpere(int eu){
        return (eu / 1000) * 3.5F;
    }

    public static int ampereToEU(float ampere){
        return Math.round((ampere * 1000) / 3.5F);
    }

    public static float getVolage(int resistance, float ampere){
        return resistance * ampere;
    }

    public static float getAmpere(int resistance, float voltage){
        return resistance / voltage;
    }

    public static int getResistance(float voltage, float ampere){
        return Math.round(voltage / ampere);
    }

}
