/*
 * This file 'ColorUtil.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.util;

/**
 * @author canitzp
 */
public enum ColorUtil{

    BLACK("Black", "#000000", 0x000000),
    WHITE("White", "#FFFFFF", 0xFFFFFF),
    MAGENDA("Magenda", "#FF00FF", 0xFF00FF),
    CYAN("Cyan", "#00FFFF", 0x00FFFF),
    YELLOW("Yellow", "#FFFF00", 0xFFFF00),
    RED("Red", "#FF0000", 0xFF0000),
    BLUE("Blue", "#0000FF", 0x0000FF),
    GREEN("Green", "#00FF00", 0x00FF00),
    GREY("Grey", "#808080", 0x808080),
    ELLPECKGREEN("Peck", "#18AE1A", 0x18AE1A),
    DOGBLESSEDBLUE("BlesseD", "#165A91", 0x165A91);

    public String colorName, colorValueName;
    public int colorValue;
    ColorUtil(String colorName, String colorValueName, int colorValue) {
        this.colorName = colorName;
        this.colorValueName = colorValueName;
        this.colorValue = colorValue;
    }

}
