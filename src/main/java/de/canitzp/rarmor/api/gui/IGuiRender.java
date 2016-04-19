package de.canitzp.rarmor.api.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public interface IGuiRender{

    void render(GuiScreen gui, int guiLeft, int guiTop, int x, int y);

    int getLength(FontRenderer fontRenderer);

}
