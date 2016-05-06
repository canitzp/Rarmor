/*
 * This file 'ColorUtil.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.api;

/**
 * @author canitzp
 */
public enum Colors {

    WHITE("White", "#FFFFFF", 0xFFFFFF),
    BLACK("Black", "#000000", 0x000000),
    MAGENDA("Magenda", "#FF00FF", 0xFF00FF),
    CYAN("Cyan", "#00FFFF", 0x00FFFF),
    YELLOW("Yellow", "#FFFF00", 0xFFFF00),
    RED("Red", "#FF0000", 0xFF0000),
    BLUE("Blue", "#0000FF", 0x0000FF),
    GREEN("Green", "#00FF00", 0x00FF00),
    GREY("Grey", "#808080", 0x808080),
    BABYBLUE("Baby blue", "#89CFF0", 0x89CFF0),
    ELLPECKGREEN("Peck", "#18AE1A", 0x18AE1A),
    DOGBLESSEDBLUE("BlesseD", "#165A91", 0x165A91),
    XOGUEBLUE("Xogueblue", "#0055FF", 0x0055FF),
    CLOBBERSTOMPMETALL("clobbermetal", "#151515", 0x151515),
    CHRISKAMINROT("Gammas red", "#A1232B", 0xA1232B),
    CHRISSHITBROWN("Gammas brown", "#45320C", 0x45320C),
    CHRISBLUE("Gammas turquoise", "#00CCCC", 0x00CCCC),
    SKYPURPLE("Skypurple", "#9600FF", 0x9600FF),
    MARCBLUE("Marcblue, ", "#0ADCFC", 0x0ADCFC),
    JACKIERED("Jackiered", "#B33636", 0xB33636)


    ;

    public String colorName, colorValueName;
    public int colorValue;
    Colors(String colorName, String colorValueName, int colorValue) {
        this.colorName = colorName;
        this.colorValueName = colorValueName;
        this.colorValue = colorValue;
    }
    public String getName(){
        return this.colorName + " " + this.colorValueName;
    }

}
