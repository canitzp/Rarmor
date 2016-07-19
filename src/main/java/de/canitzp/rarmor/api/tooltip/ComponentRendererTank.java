package de.canitzp.rarmor.api.tooltip;

import de.canitzp.rarmor.api.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fluids.FluidStack;

import static de.canitzp.rarmor.api.GuiUtils.utilsLoc;

/**
 * @author canitzp
 */
public class ComponentRendererTank implements IComponentRender{

    private int maxCap;
    private FluidStack fluidStack;
    private String s;

    public ComponentRendererTank(FluidStack stack, int maxCap){
        this.fluidStack = stack;
        this.maxCap = maxCap;
        this.s = stack != null ? stack.getLocalizedName() + " " + stack.amount + "/" + maxCap + "mB" : "No Fluid";
    }

    @Override
    public void render(FontRenderer fontRenderer, int x, int y, int color) {
        int x1 = (x-21-fontRenderer.getStringWidth(s)-2)/2;
        fontRenderer.drawString(s, x1, y, color, true);
        int x2 = (x-21+fontRenderer.getStringWidth(s))/2;
        int factor = 0;
        if(fluidStack != null){
            factor = fluidStack.amount * 21 / maxCap;
            GuiUtils.drawFluid(fluidStack.getFluid(), x2, y-1, factor, 10);
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(utilsLoc);
        GuiUtils.drawTexturedModalRect(x2, y - 1, 26, 41, 21, 10);
    }
}
