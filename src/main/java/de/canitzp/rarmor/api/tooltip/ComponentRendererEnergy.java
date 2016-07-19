package de.canitzp.rarmor.api.tooltip;

import de.canitzp.rarmor.api.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import static de.canitzp.rarmor.api.GuiUtils.utilsLoc;

/**
 * @author canitzp
 */
public class ComponentRendererEnergy implements IComponentRender {

    private int currentEnergy, maxEnergy;
    private String s;

    public ComponentRendererEnergy(int currentEnergy, int maxEnergy, String energyName){
        this.currentEnergy = currentEnergy;
        this.maxEnergy = maxEnergy;
        this.s = currentEnergy + "/" + maxEnergy + energyName;
    }

    @Override
    public void render(FontRenderer fontRenderer, int x, int y, int color) {
        int x1 = (x-21-fontRenderer.getStringWidth(s)-2)/2;
        fontRenderer.drawString(s, x1, y, color, true);
        Minecraft.getMinecraft().getTextureManager().bindTexture(utilsLoc);
        int x2 = (x-21+fontRenderer.getStringWidth(s))/2;
        GuiUtils.drawTexturedModalRect(x2, y - 1, 26, 21, 21, 10);
        int factor = currentEnergy * 20 / maxEnergy;
        GuiUtils.drawTexturedModalRect(x2, y - 1, 26, 31, factor+1, 10);
    }

}
