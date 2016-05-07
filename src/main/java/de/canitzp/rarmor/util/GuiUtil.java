/*
 * This file 'GuiUtil.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

/**
 * @author canitzp
 */
public class GuiUtil{

    private static float zLevel;

    public static void drawBarHorizontal(ResourceLocation batRes, ResourceLocation guiLoc, int x, int y, int textureX, int textureY, int width, int height, int maxCapacity, int amount){
        int factor = amount * height / maxCapacity;
        drawTexturedModalRect(batRes, guiLoc, x, y - factor, textureX, textureY - factor, width, factor);
    }

    public static void drawBarVertical(ResourceLocation batRes, ResourceLocation guiLoc, int x, int y, int textureX, int textureY, int width, int height, int maxCapacity, int amount){
        int factor = (amount * width) / maxCapacity;
        drawTexturedModalRect(batRes, guiLoc, x, y - height, textureX, textureY - 10, factor + 1, height);
    }

    public static void bindTexture(ResourceLocation location){
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
    }

    public static void drawTexturedModalRect(ResourceLocation batRes, ResourceLocation guiLoc, int x, int y, int textureX, int textureY, int width, int height){
        bindTexture(batRes);
        drawTexturedModalRect(x, y, textureX, textureY, width, height);
        bindTexture(guiLoc);
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height){
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double) (x), (double) (y + height), (double) zLevel).tex((double) ((float) (textureX) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) (y + height), (double) zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) (y), (double) zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY) * f1)).endVertex();
        worldrenderer.pos((double) (x), (double) (y), (double) zLevel).tex((double) ((float) (textureX) * f), (double) ((float) (textureY) * f1)).endVertex();
        tessellator.draw();
    }

    public static void drawFluid(Fluid fluid, int x, int y, int textureX, int textureY, int width, int height){
        ResourceLocation fluidTexture = new ResourceLocation(fluid.getStill().getResourceDomain(), "textures/" + fluid.getStill().getResourcePath() + ".png");
        bindTexture(fluidTexture);
        drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }

}
