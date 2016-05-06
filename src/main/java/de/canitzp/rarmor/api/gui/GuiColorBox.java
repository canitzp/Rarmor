/*
 * This file 'GuiColorBox.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.api.gui;

import de.canitzp.rarmor.api.Colors;
import de.canitzp.rarmor.api.RarmorResources;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;

/**
 * Created by canitzp on 06.05.16.
 */
public class GuiColorBox extends Gui{

    public Colors color;
    public int x, y, width = 8, height = 8;
    private ResourceLocation iconLoc = RarmorResources.GUIELEMENTS.getNewLocation();

    public GuiColorBox(Colors color, int x, int y){
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public void render(GuiContainer gui){
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.color.colorValue;
        float f = (float)(i >> 16 & 255) / 255.0F;
        float f1 = (float)(i >> 8 & 255) / 255.0F;
        float f2 = (float)(i & 255) / 255.0F;
        GlStateManager.color(1.0F * f, 1.0F * f1, 1.0F * f2, 1.0F);
        gui.mc.getTextureManager().bindTexture(iconLoc);
        this.drawTexturedModalRect(this.x + gui.guiLeft, this.y + gui.guiTop, 0, 0, 8, 8);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public void drawScreen(GuiContainer gui, int mouseX, int mouseY){
        if(isMouseOver(gui, mouseX, mouseY)){
            gui.drawHoveringText(Collections.singletonList(this.color.colorName + " " + this.color.colorValueName), mouseX, mouseY);
        }
    }

    public boolean isMouseOver(GuiContainer gui, int mouseX, int mouseY){
        if(mouseX >= x + gui.guiLeft && mouseY >= y + gui.guiTop) {
            if (mouseX <= x + width + gui.guiLeft && mouseY <= y + height + gui.guiTop) {
                return true;
            }
        }
        return false;
    }

    public Colors onClick(GuiContainer gui, int mouseX, int mouseY){
        if(isMouseOver(gui, mouseX, mouseY)) {
            return this.color;
        }
        return null;
    }

}
