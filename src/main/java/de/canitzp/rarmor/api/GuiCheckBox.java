package de.canitzp.rarmor.api;

import de.canitzp.rarmor.util.ColorUtil;
import de.canitzp.rarmor.util.GuiUtil;
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
public class GuiCheckBox extends Gui implements IGuiRender{

    public ResourceLocation iconLocation;
    public int x, y, width, height, color;
    public GuiScreen guiContainer;
    public String text = "";
    public List<String> description;
    private boolean isActivated;
    private static List<GuiCheckBox> checkBoxList = new ArrayList<>();

    public GuiCheckBox(GuiScreen guiContainer, ResourceLocation iconLocation, int x, int y, int width, int height, String text, List<String> description, int color) {
        this.x = x;
        this.y = y;
        this.guiContainer = guiContainer;
        this.text = text;
        this.description = description;
        this.iconLocation = iconLocation;
        this.width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.text) + 9;
        this.height = height;
        checkBoxList.add(this);
        this.color = color;
    }

    public GuiCheckBox(GuiScreen guiContainer, ResourceLocation iconLocation, int x, int y, int width, int height, String text, List<String> description) {
       this(guiContainer, iconLocation, x, y, width, height, text, description, ColorUtil.BLACK);
    }

    public void drawCheckBox(int guiLeft, int guiTop){
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.fontRendererObj.drawString(text, guiLeft + x + 9, guiTop + y + 1, this.color);
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

    public static GuiCheckBox getCheckBoxAtCoordinates(int x, int y){
        for(GuiCheckBox checkBox : checkBoxList){
            if(checkBox.x == x && checkBox.y == y){
                return checkBox;
            }
        }
        return null;
    }

    @Override
    public void render(GuiScreen gui, int guiLeft, int guiTop, int x, int y) {
        this.x = x;
        this.y = y;
        this.drawCheckBox(0, 0);
    }

    @Override
    public int getLength(FontRenderer fontRenderer) {
        return 10 + fontRenderer.getStringWidth(this.text);
    }
}