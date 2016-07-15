package de.canitzp.rarmor.api;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author canitzp
 */
public class GuiUtils{

    public static final ResourceLocation utilsLoc = new ResourceLocation("rarmor", "textures/gui/guiTabUtil.png");

    public static void drawSlot(GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 0, 26, 18, 18);
    }

    public static void drawBigSlot(GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 0, 0, 26, 26);
    }

    public static void drawSlotField(GuiScreen gui, int x, int y, int amountX, int amountY){
        for(int i = 0; i < amountX; i++){
            for(int j = 0; j < amountY; j++){
                drawSlot(gui, x + (i*18), y + (j*18));
            }
        }
    }

    public static void drawCraftingArrow(GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 46, 0, 15, 22);
    }

    public static void drawToastButton(GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 62, 0, 16, 16);
    }

    public static void drawBattery(GuiScreen gui, int x, int y, int maxEnergy, int currentEnergy){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        int factor = currentEnergy * 21 / maxEnergy;
        gui.drawTexturedModalRect(x, y - factor, 0, 21 - factor, 10, factor);
    }

    public static void drawBurnFlame(GuiScreen gui, int x, int y, int maxBurnTime, int currentBurnTime){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 77, 0, 14, 14);
        if(maxBurnTime <= 1){
            return;
        }
        int factor = currentBurnTime * 14 / maxBurnTime;
        gui.drawTexturedModalRect(x-1, y - factor + 13, 77, 28 - factor, 14, factor);
    }
    public static void drawEnergyBar(GuiScreen gui, int x,int y, int maxEnergy, int currentEnergy){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        gui.drawTexturedModalRect(x, y, 33, 127, 33, 129);
        if(maxEnergy <= 1){
            return;
        }
        int factor = currentEnergy * 129 / maxEnergy;
        gui.drawTexturedModalRect(x, y + 129 - factor, 0, 127 + 129 - factor, 33, factor);
    }

    public static void drawEnergyField(GuiScreen gui, int x, int y, int maxEnergy, int currentEnergy){
        drawEnergyBar(gui, x, y, maxEnergy, currentEnergy);
        drawSlotField(gui, x + 13, y + 45, 1, 2);
        drawIcon(ICONS.THUNDERBOLT, gui, x + 17, y + 47);
        drawIcon(ICONS.THUNDERBOLT, gui, x + 17, y + 65);
    }

    public static IInventory addEnergyField(List<Slot> slots, IInventory inventory, int x, int y){
        slots.add(new Slot(inventory, 0, x + 14, y + 46));
        return inventory;
    }

    public static void drawIcon(ICONS icon, GuiScreen gui, int x, int y){
        gui.mc.getTextureManager().bindTexture(utilsLoc);
        icon.draw(gui, x, y);
    }

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

}
