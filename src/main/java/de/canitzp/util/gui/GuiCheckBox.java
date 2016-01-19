package de.canitzp.util.gui;

import de.canitzp.util.util.ColorUtil;
import de.canitzp.util.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author canitzp
 */
public class GuiCheckBox extends Gui{

    public ResourceLocation iconLocation;
    public int x, y, width, height;
    public GuiContainer guiContainer;
    public String text;
    public List<String> description;
    private boolean isActivated;

    public GuiCheckBox(GuiContainer guiContainer, ResourceLocation iconLocation, int x, int y, int width, int height, String text, List<String> description) {
        this.x = x;
        this.y = y;
        this.guiContainer = guiContainer;
        this.text = text;
        this.description = description;
        this.iconLocation = iconLocation;
        this.width = width;
        this.height = height;
    }

    public void drawCheckBox(int guiLeft, int guiTop){
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.fontRendererObj.drawString(text, guiLeft + x + 9, guiTop + y + 1, ColorUtil.BLACK);
        minecraft.getTextureManager().bindTexture(iconLocation);
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

    public void mouseOverEvent(int mouseX, int mouseY, int guiLeft, int guiTop, FontRenderer fontRenderer){
        if(description != null && mouseX >= x + guiLeft && mouseY >= y + guiTop){
            if(mouseX <= x + width + guiLeft && mouseY <= y + height + guiTop){
                GuiUtil.drawHoveringText(guiContainer, description, mouseX, mouseY, fontRenderer);
            }
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int guiLeft, int guiTop){
        if(mouseX >= x + guiLeft && mouseY >= y + guiTop) {
            if (mouseX <= x + width + guiLeft && mouseY <= y + height + guiTop) {
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