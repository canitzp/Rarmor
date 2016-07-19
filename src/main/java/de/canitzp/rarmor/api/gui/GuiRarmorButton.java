package de.canitzp.rarmor.api.gui;

import de.canitzp.rarmor.api.GuiUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class GuiRarmorButton implements IGuiRenderer{

    private int x, y, width, height, outlineColor, textColor;
    private String text;

    public GuiRarmorButton(int x, int y, int width, int height, String text, int outlineColor, int textColor){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.outlineColor = outlineColor;
        this.textColor = textColor;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(GuiScreen gui, int guiLeft, int guiTop){
        FontRenderer fontRenderer = gui.mc.fontRendererObj;
        Gui.drawRect(x + guiLeft, y + guiTop, x + width + guiLeft, y + height + guiTop, outlineColor);
        //GuiUtils.drawGradientRect(x+guiLeft, y+height+guiTop, x+width+guiLeft, y+height+guiTop, outlineColor, outlineColor);
        //GuiUtils.drawGradientRect(x+guiLeft, y+guiTop, x+guiLeft, y+height+guiTop, outlineColor, outlineColor);
        //GuiUtils.drawGradientRect(x+width+guiLeft, y+guiTop, x+width+guiLeft, y+height+guiTop, outlineColor, outlineColor);
        //fontRenderer.drawString(text, ((width/2)-fontRenderer.getStringWidth(text))/2 + x + guiLeft, y + guiTop, textColor);
    }

}
