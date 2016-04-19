package de.canitzp.rarmor.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.util.List;

/**
 * @author canitzp
 */
public class GuiUtil{

    private static float zLevel;

    public static void drawHoveringText(GuiScreen guiScreen, List<String> textLines, int x, int y, FontRenderer font){
        if (!textLines.isEmpty()){
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;
            for (String s : textLines){
                int j = font.getStringWidth(s);
                if (j > i){
                    i = j;
                }
            }
            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;
            if (textLines.size() > 1){
                k += 2 + (textLines.size() - 1) * 10;
            }
            if (l1 + i > guiScreen.width){
                l1 -= 28 + i;
            }
            if (i2 + k + 6 > guiScreen.height){
                i2 = guiScreen.height - k - 6;
            }
            zLevel = 300.0F;
            int l = -267386864;
            drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
            drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
            drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
            drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
            drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
            int i1 = 1347420415;
            int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
            drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
            drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
            drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
            drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);
            for (int k1 = 0; k1 < textLines.size(); ++k1){
                String s1 = textLines.get(k1);
                font.drawStringWithShadow(s1, (float) l1, (float) i2, -1);
                if (k1 == 0){
                    i2 += 2;
                }
                i2 += 10;
            }
            zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor){
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double) right, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double) left, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double) left, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos((double) right, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

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
