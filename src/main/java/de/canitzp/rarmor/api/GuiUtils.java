package de.canitzp.rarmor.api;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class GuiUtils{

    public static final ResourceLocation utilsLoc = new ResourceLocation("rarmor", "textures/gui/guiTabUtil.png");

    public static void drawSlot(GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 0, 26, 18, 18);
    }

    public static void drawBigSlot(GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 0, 0, 26, 26);
    }

    public static void drawSlotField(GuiScreen gui, int x, int y, int amountX, int amountY){
        for(int i = 0; i < amountX; i++){
            for(int j = 0; j < amountY; j++){
                drawSlot(gui, x + (i*18), y + (j*18));
            }
        }
    }

    public static void drawCraftingArrow(GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 46, 0, 15, 22);
    }

    public static void drawBattery(GuiScreen gui, int x, int y, int maxEnergy, int currentEnergy){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        int factor = currentEnergy * 21 / maxEnergy;
        gui.drawTexturedModalRect(x, y - factor, 0, 21 - factor, 10, factor);
    }

}
