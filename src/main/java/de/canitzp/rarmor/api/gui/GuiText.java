package de.canitzp.rarmor.api.gui;

import de.canitzp.rarmor.util.MinecraftUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author canitzp
 */
public class GuiText extends Gui implements IGuiRender {

    private String text;
    private int color;
    private FontRenderer fontRenderer = MinecraftUtil.getFontRenderer();

    public GuiText(String text, int color) {
        this.text = text;
        this.color = color;
    }

    @Override
    public void render(GuiScreen gui, int guiLeft, int guiTop, int x, int y) {
        fontRenderer.drawString(this.text, x, y, this.color);
    }

    @Override
    public int getLength(FontRenderer fontRenderer) {
        return fontRenderer.getStringWidth(this.text);
    }
}
