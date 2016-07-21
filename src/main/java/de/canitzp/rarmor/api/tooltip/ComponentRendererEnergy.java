package de.canitzp.rarmor.api.tooltip;

import de.canitzp.rarmor.GuiIWTSettings;
import de.canitzp.rarmor.api.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import static de.canitzp.rarmor.api.GuiUtils.utilsLoc;

/**
 * @author canitzp
 */
public class ComponentRendererEnergy implements IComponentRender {

    private int currentEnergy, maxEnergy, hexColor;
    private String s;

    public ComponentRendererEnergy(int currentEnergy, int maxEnergy, String energyName){
        this.currentEnergy = currentEnergy;
        this.maxEnergy = maxEnergy;
        this.s = currentEnergy + "/" + maxEnergy + " " + energyName;
        this.setEnergyColor(0xFF0000);
    }

    @Override
    public void render(FontRenderer fontRenderer, int x, int y, int color) {
        int x1 = (x/2 - fontRenderer.getStringWidth(s)/2)-12 + GuiIWTSettings.offsetX;
        int x2 = x1 + fontRenderer.getStringWidth(s) + 2;
        int factor = currentEnergy * 20 / maxEnergy;
        fontRenderer.drawString(s, x1, y, color, true);
        Minecraft.getMinecraft().getTextureManager().bindTexture(utilsLoc);
        GuiUtils.glcolor(this.hexColor);
        GuiUtils.drawTexturedModalRect(x2, y - 1, 26, 31, factor+1, 10);
        GuiUtils.glcolor();
        GuiUtils.drawTexturedModalRect(x2, y - 1, 26, 21, 21, 10);
    }

    public ComponentRendererEnergy setEnergyColor(int hexValue){
        this.hexColor = hexValue;
        return this;
    }

}
