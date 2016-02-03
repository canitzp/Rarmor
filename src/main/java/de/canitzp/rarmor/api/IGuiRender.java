package de.canitzp.rarmor.api;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public interface IGuiRender {

    @SideOnly(Side.CLIENT)
    void render(GuiScreen gui, int guiLeft, int guiTop, int x, int y);

    @SideOnly(Side.CLIENT)
    int getLength(FontRenderer fontRenderer);

}
