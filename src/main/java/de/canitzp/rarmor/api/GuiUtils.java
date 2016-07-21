package de.canitzp.rarmor.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
public class GuiUtils{

    public static final ResourceLocation utilsLoc = new ResourceLocation("rarmor", "textures/gui/guiTabUtil.png");

    @SideOnly(Side.CLIENT)
    public static void drawSlot(GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 0, 26, 18, 18);
    }

    @SideOnly(Side.CLIENT)
    public static void drawBigSlot(GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 0, 0, 26, 26);
    }

    @SideOnly(Side.CLIENT)
    public static void drawSlotField(GuiScreen gui, int x, int y, int amountX, int amountY){
        for(int i = 0; i < amountX; i++){
            for(int j = 0; j < amountY; j++){
                drawSlot(gui, x + (i*18), y + (j*18));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void drawCraftingArrow(GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 46, 0, 15, 22);
    }

    @SideOnly(Side.CLIENT)
    public static void drawToastButton(GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 62, 0, 16, 16);
    }

    @SideOnly(Side.CLIENT)
    public static void drawCheckBox(GuiScreen gui, int x, int y, boolean state){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 91, state ? 9 : 0, 9, 9);
    }

    @SideOnly(Side.CLIENT)
    public static void drawBattery(GuiScreen gui, int x, int y, int maxEnergy, int currentEnergy){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        int factor = currentEnergy * 21 / maxEnergy;
        gui.drawTexturedModalRect(x, y - factor, 0, 21 - factor, 10, factor);
    }

    @SideOnly(Side.CLIENT)
    public static void drawBurnFlame(GuiScreen gui, int x, int y, int maxBurnTime, int currentBurnTime){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 77, 0, 14, 14);
        if(maxBurnTime <= 1){
            return;
        }
        int factor = currentBurnTime * 14 / maxBurnTime;
        gui.drawTexturedModalRect(x-1, y - factor + 13, 77, 28 - factor, 14, factor);
    }

    @SideOnly(Side.CLIENT)
    public static void drawEnergyBar(GuiScreen gui, int x,int y, int maxEnergy, int currentEnergy){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 33, 127, 33, 129);
        if(maxEnergy <= 1){
            return;
        }
        int factor = currentEnergy * 129 / maxEnergy;
        gui.drawTexturedModalRect(x, y + 129 - factor, 0, 127 + 129 - factor, 33, factor);
    }

    @SideOnly(Side.CLIENT)
    public static void drawEnergyField(GuiScreen gui, int x, int y, int maxEnergy, int currentEnergy){
        drawEnergyBar(gui, x, y, maxEnergy, currentEnergy);
        drawSlotField(gui, x + 13, y + 45, 1, 2);
        drawIcon(ICONS.THUNDERBOLT, gui, x + 17, y + 47);
        drawIcon(ICONS.THUNDERBOLT, gui, x + 17, y + 65);
    }

    public static IInventory addEnergyField(List<Slot> slots, IInventory inventory, int x, int y){
        slots.add(new Slot(inventory, 0, x + 14, y + 46));
        slots.add(new Slot(inventory, 1, x + 14, y + 46 + 18));
        return inventory;
    }

    @SideOnly(Side.CLIENT)
    public static void drawIcon(ICONS icon, GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        icon.draw(gui, x, y);
    }

    @SideOnly(Side.CLIENT)
    enum ICONS{
        THUNDERBOLT(0, 44, 10, 14);

        public int x, y, width, height;
        ICONS(int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        public void draw(GuiScreen gui, int x, int y){
            gui.drawTexturedModalRect(x, y, this.x, this.y, this.width, this.height);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void drawHoveringText(GuiScreen gui, int x, int y, List<String> textLines){
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;
            for (String s : textLines) {
                int j = gui.mc.fontRendererObj.getStringWidth(s);
                if (j > i) {
                    i = j;
                }
            }
            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;
            if (textLines.size() > 1) {
                k += 2 + (textLines.size() - 1) * 10;
            }
            if (l1 + i > gui.width) {
                l1 -= 28 + i;
            }
            if (i2 + k + 6 > gui.height) {
                i2 = gui.height - k - 6;
            }
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
            for (int k1 = 0; k1 < textLines.size(); ++k1) {
                String s1 = textLines.get(k1);
                gui.mc.fontRendererObj.drawStringWithShadow(s1, (float)l1, (float)i2, -1);
                if (k1 == 0) {
                    i2 += 2;
                }
                i2 += 10;
            }
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    @SideOnly(Side.CLIENT)
    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos((double)right, (double)top, 0).color(f1, f2, f3, f).endVertex();
        vertexbuffer.pos((double)left, (double)top, 0).color(f1, f2, f3, f).endVertex();
        vertexbuffer.pos((double)left, (double)bottom, 0).color(f5, f6, f7, f4).endVertex();
        vertexbuffer.pos((double)right, (double)bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    @SideOnly(Side.CLIENT)
    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(x), (double)(y + height), 0).tex((double)((float)(textureX) * f), (double)((float)(textureY + height) * f1)).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + height), 0).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y), 0).tex((double)((float)(textureX + width) * f), (double)((float)(textureY) * f1)).endVertex();
        vertexbuffer.pos((double)(x), (double)(y), 0).tex((double)((float)(textureX) * f), (double)((float)(textureY) * f1)).endVertex();
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public static void drawFluid(Fluid fluid, int x, int y, int width, int height){
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(fluid.getStill().getResourceDomain(), "textures/" + fluid.getStill().getResourcePath() + ".png"));
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, 16, 512);
    }

    @SideOnly(Side.CLIENT)
    public static void glcolor(int hexValue){
        float r = (float)(hexValue >> 16 & 255) / 255.0F;
        float g = (float)(hexValue >> 8 & 255) / 255.0F;
        float b = (float)(hexValue & 255) / 255.0F;
        float alpha = (float)(hexValue >> 24 & 255) / 255.0F;
        glcolor(r, g, b, alpha);
    }

    @SideOnly(Side.CLIENT)
    public static void glcolor(float r, float g, float b, float alpha){
        GlStateManager.color(r, g, b, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public static void glcolor(){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
