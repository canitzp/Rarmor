package de.canitzp.rarmor.api.gui;

import de.canitzp.rarmor.api.GuiUtils;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
public class GuiCheckBox{

    private int x;
    private int y;
    private String text;
    private List<String> hoveringText;
    private boolean isActive = false;

    public GuiCheckBox(int x, int y, String text, List<String> hoveringText){
        this.x = x;
        this.y = y;
        this.text = text;
        this.hoveringText = hoveringText;
    }

    @SideOnly(Side.CLIENT)
    public void draw(GuiScreen gui, int guiLeft, int guiTop){
        gui.mc.fontRendererObj.drawString(this.text, guiLeft + x + 10, guiTop + y + 1, MapColor.BLACK.colorValue);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiUtils.drawCheckBox(gui, guiLeft + x, guiTop + y, this.isActive);
        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    public void drawForeground(GuiScreen gui, int guiLeft, int guiTop, int mouseX, int mouseY){
        if(isMouseOver(guiLeft, guiTop, mouseX, mouseY)){
            GuiUtils.drawHoveringText(gui, mouseX, mouseY, this.hoveringText);
        }
    }

    @SideOnly(Side.CLIENT)
    public void onMouseClick(int guiLeft, int guiTop, int mouseX, int mouseY){
        if(isMouseOver(guiLeft, guiTop, mouseX, mouseY)) {
            this.isActive = !this.isActive;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean isMouseOver(int guiLeft, int guiTop, int mouseX, int mouseY){
        if(mouseX >= x + guiLeft && mouseY >= y + guiTop){
            if(mouseX <= x + Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 10 + guiLeft && mouseY <= y + 10 + guiTop){
                return true;
            }
        }
        return false;
    }

    public void setState(boolean state){
        this.isActive = state;
    }

    public boolean getState(){
        return this.isActive;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        nbt.setBoolean(this.text.replace(" ", "").toLowerCase(), this.isActive);
        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt){
        this.isActive = nbt.getBoolean(this.text.replace(" ", "").toLowerCase());
    }

}
