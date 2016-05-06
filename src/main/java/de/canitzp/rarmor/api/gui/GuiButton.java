/*
 * This file 'GuiButton.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.api.gui;

import de.canitzp.rarmor.api.RarmorResources;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by canitzp on 06.05.16.
 */
public class GuiButton{

    public int x, y, width, height;
    private ResourceLocation iconLoc = RarmorResources.GUIELEMENTS.getNewLocation();

    public GuiButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(GuiContainer gui){
        int i = 0xFFFF00;
        float f = (float)(i >> 16 & 255) / 255.0F;
        float f1 = (float)(i >> 8 & 255) / 255.0F;
        float f2 = (float)(i & 255) / 255.0F;
        GlStateManager.color(1.0F*f, 1.0F*f1, 1.0F*f2, 1.0F);
        gui.mc.getTextureManager().bindTexture(this.iconLoc);
        gui.drawTexturedModalRect(this.x + gui.guiLeft, this.y + gui.guiTop, 0, 16, 16, 16);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean isClicked(GuiContainer gui, int mouseX, int mouseY){
        if(mouseX >= x + gui.guiLeft && mouseY >= y + gui.guiTop) {
            if (mouseX <= x + width + gui.guiLeft && mouseY <= y + height + gui.guiTop) {
                return true;
            }
        }
        return false;
    }

}
