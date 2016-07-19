package de.canitzp.rarmor.api.tooltip;

import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public interface IComponentRender {

    @SideOnly(Side.CLIENT)
    void render(FontRenderer fontRenderer, int x, int y, int color);

}
