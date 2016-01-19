package de.canitzp.rarmor.inventory.gui;

import com.google.common.collect.Lists;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.util.util.ColorUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author canitzp
 */
public class GuiDigitalManual extends GuiScreen {

    private EntityPlayer player;
    private ResourceLocation normalLoc = new ResourceLocation(Rarmor.MODID, "textures/gui/guiDigitalManual.png");
    public int xSize;
    public int ySize;
    public int guiLeft;
    public int guiTop;

    public GuiDigitalManual(EntityPlayer player){
        this.player = player;
        this.xSize = 251;
        this.ySize = 151;
    }

    @Override
    public void initGui() {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        this.mc.getTextureManager().bindTexture(normalLoc);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        renderText(Lists.newArrayList(">What is Rarmor?", " Rarmor is a little Minecraft Mod, that adds", " a cool modular upgradeable electric Armor."));
        //super.drawScreen(x, y, f);
    }

    //6,6
    public void renderText(List<String> list){
        int x = guiLeft + 6;
        int y = guiTop + 6;
        for(String string : list){
            fontRendererObj.drawString(string, x, y, ColorUtil.WHITE);
            y += 10;
        }
    }
}
