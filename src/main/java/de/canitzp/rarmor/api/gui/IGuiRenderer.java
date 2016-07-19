package de.canitzp.rarmor.api.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public interface IGuiRenderer {

    @SideOnly(Side.CLIENT)
    void render(GuiScreen gui, int guiLeft, int guiTop);

}
