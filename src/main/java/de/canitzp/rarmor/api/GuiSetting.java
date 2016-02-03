package de.canitzp.rarmor.api;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiSetting extends GuiScreen {

    public List<GuiTextField> textFields = Lists.newArrayList();
    public List<IGuiRender> renderer = Lists.newArrayList();
    private int length = 0;

    public void init(){
        renderer.addAll(textFields);
        for(IGuiRender render : renderer){
            int i = render.getLength(Minecraft.getMinecraft().fontRendererObj);
            if(i > this.length){
                this.length = i;
            }
        }
    }

    public void render(GuiScreen screen, ElectricalComponent component, int guiLeft, int guiTop, int x, int y){
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        this.zLevel = 300.0F;
        int l1 = x + 12;
        int k = 10 * renderer.size();
        int l = -267386864;
        this.drawGradientRect(l1 - 3, y - 4, l1 + this.length + 3, y - 3, l, l);
        this.drawGradientRect(l1 - 3, y + k + 3, l1 + this.length + 3, y + k + 4, l, l);
        this.drawGradientRect(l1 - 3, y - 3, l1 + this.length + 3, y + k + 3, l, l);
        this.drawGradientRect(l1 - 4, y - 3, l1 - 3, y + k + 3, l, l);
        this.drawGradientRect(l1 + this.length + 3, y - 3, l1 + this.length + 4, y + k + 3, l, l);
        int i1 = 1347420415;
        int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
        this.drawGradientRect(l1 - 3, y - 3 + 1, l1 - 3 + 1, y + k + 3 - 1, i1, j1);
        this.drawGradientRect(l1 + this.length + 2, y - 3 + 1, l1 + this.length + 3, y + k + 3 - 1, i1, j1);
        this.drawGradientRect(l1 - 3, y - 3, l1 + this.length + 3, y - 3 + 1, i1, i1);
        this.drawGradientRect(l1 - 3, y + k + 2, l1 + this.length + 3, y + k + 3, j1, j1);
        component.width = length;
        component.height = k;
        for(int i = 0; i < this.renderer.size(); ++i){
            IGuiRender render = this.renderer.get(i);
            if(render != null){
                render.render(screen, guiLeft, guiTop, x + 12, y + ((i - 1) * 10) + 10);
            }
        }
        this.zLevel = 0.0F;
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }

}
