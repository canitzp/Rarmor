package de.canitzp.rarmor.inventory.gui;

import de.canitzp.rarmor.api.IGuiRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author canitzp
 */
public class GuiTextField extends net.minecraft.client.gui.GuiTextField implements IGuiRender{

    public GuiTextField(int componentId, int x, int y, int width, int height) {
        super(componentId, Minecraft.getMinecraft().fontRendererObj, x, y, width, height);
    }

    @Override
    public void render(GuiScreen gui, int guiLeft, int guiTop) {
        this.drawTextBox();
    }
}
