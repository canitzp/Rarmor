package de.canitzp.rarmor.inventory.gui;

import de.canitzp.rarmor.api.IGuiRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author canitzp
 */
public class GuiText extends Gui implements IGuiRender {

    private String text;
    private int x, y, color;

    public GuiText(String text, int x, int y, int color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void render(GuiScreen gui, int guiLeft, int guiTop) {
        Minecraft.getMinecraft().fontRendererObj.drawString(this.text, this.x, this.y, this.color);
    }
}
