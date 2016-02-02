package de.canitzp.rarmor.api;

import com.google.common.collect.Lists;
import de.canitzp.util.util.ColorUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

import java.util.List;

/**
 * @author canitzp
 */
public class GuiSetting extends GuiScreen {

    public List<IGuiRender> renderer = Lists.newArrayList();

    public void render(GuiScreen screen, int guiLeft, int guiTop, int x, int y, int lenght){
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        this.zLevel = 300.0F;
        //this.itemRender.zLevel = 300.0F;
        int l1 = x + 12;
        int i2 = y - 12;
        int i = lenght;
        int k = 10 * renderer.size();
        int l = -267386864;
        this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
        this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
        this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
        this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
        this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
        int i1 = 1347420415;
        int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
        this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
        this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
        this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
        this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);

        for(IGuiRender render : renderer){
            if(render != null){
                render.render(screen, guiLeft, guiTop);
            }
        }
        this.zLevel = 0.0F;
        //this.itemRender.zLevel = 0.0F;
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }

}
