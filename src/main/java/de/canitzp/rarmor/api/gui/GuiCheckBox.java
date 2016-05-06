/*
 * This file 'GuiCheckBox.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.api.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiCheckBox extends Gui {

    private static List<GuiCheckBox> checkBoxList = new ArrayList<>();
    public ResourceLocation iconLocation;
    public int x, y, width, height, color;
    public GuiScreen guiContainer;
    public String text = "";
    public List<String> description;
    private boolean isActivated;
    private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

    public GuiCheckBox(GuiScreen guiContainer, ResourceLocation iconLocation, int x, int y, int height, String text, List<String> description, int color){
        this.x = x;
        this.y = y;
        this.guiContainer = guiContainer;
        this.text = text;
        this.description = description;
        this.iconLocation = iconLocation;
        this.width = fontRenderer.getStringWidth(this.text) + 9;
        this.height = height;
        checkBoxList.add(this);
        this.color = color;
    }

    public GuiCheckBox(GuiScreen guiContainer, ResourceLocation iconLocation, int x, int y, int height, String text, List<String> description){
        this(guiContainer, iconLocation, x, y, height, text, description, 0);
    }

    public GuiCheckBox(ResourceLocation iconLocation, int x, int y, int height, String text, List<String> description){
        this(null, iconLocation, x, y, height, text, description);
    }

    public void drawCheckBox(int guiLeft, int guiTop){
        fontRenderer.drawString(text, guiLeft + x + 9, guiTop + y + 1, this.color);
        Minecraft.getMinecraft().getTextureManager().bindTexture(iconLocation);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        if(!isActivated){
            this.drawTexturedModalRect(this.x + guiLeft, this.y + guiTop, 0, 0, 8, 8);
        } else {
            this.drawTexturedModalRect(this.x + guiLeft, this.y + guiTop, 0, 8, 8, 8);
        }
    }

    public void mouseOverEvent(int mouseX, int mouseY, int guiLeft, int guiTop){
        if(description != null && mouseX >= x + guiLeft && mouseY >= y + guiTop){
            if(mouseX <= x + width + guiLeft && mouseY <= y + height + guiTop){
                guiContainer.drawHoveringText(description, mouseX, mouseY);
            }
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int guiLeft, int guiTop){
        if(mouseX >= x + guiLeft && mouseY >= y + guiTop){
            if(mouseX <= x + width + guiLeft && mouseY <= y + height + guiTop){
                this.isActivated = !this.isActivated;
                return true;
            }
        }
        return false;
    }

    public boolean isClicked(){
        return this.isActivated;
    }

    public void setClicked(boolean b){
        this.isActivated = b;
    }

}