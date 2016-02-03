package de.canitzp.rarmor.api;

import com.google.common.collect.Lists;
import de.canitzp.util.util.ColorUtil;
import de.canitzp.util.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.List;

/**
 * @author canitzp
 */
public class ElectricalComponent implements IElectricalComponent, IGuiInteraction {

    public ResourceLocation location = RamorResources.ELECTRICALCOMPONENTS.getNewLocation();
    public int x, y, width, height, textureX, textureY, textureW, textureH;
    public String hoveringText;

    public ElectricalComponent(int x, int y, int width, int height, int textureX, int textureY, String hoveringText) {
        this.x = x;
        this.y = y;
        this.width = this.textureW = width;
        this.height = this.textureH = height;
        this.textureX = textureX;
        this.textureY = textureY;
        this.hoveringText = hoveringText;
    }

    @Override
    public ResourceLocation guiComponent() {
        return this.location;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getTextureWidth() {
        return this.textureW;
    }

    @Override
    public int getTextureHeight() {
        return this.textureH;
    }

    @Override
    public int getTextureX() {
        return this.textureX;
    }

    @Override
    public int getTextureY() {
        return this.textureY;
    }

    @Override
    public List<String> getHoveringText() {
        return Lists.newArrayList(this.hoveringText);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void mouseHover(GuiScreen gui, GuiSetting setting, World world, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY){
        if(this.getX() + guiLeft <= mouseX && this.getX() + guiLeft + this.getTextureWidth() >= mouseX) {
            if (this.getY() + guiTop <= mouseY && this.getY() + guiTop + this.getTextureHeight() >= mouseY) {
                GuiUtil.drawHoveringText(gui, this.getHoveringText(), mouseX, mouseY, Minecraft.getMinecraft().fontRendererObj);
            }
        }
    }

    @Override
    public boolean onKeyPressed(GuiScreen gui, GuiSetting setting, World world, EntityPlayer player, char typedChar, int keyCode) {
        if(!setting.textFields.isEmpty()){
            for(GuiTextField field : setting.textFields){
                if(field.textboxKeyTyped(typedChar, keyCode)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void mouseClicked(GuiScreen gui, GuiSetting setting, World world, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, int mouseButton) throws IOException {
        if(!setting.textFields.isEmpty()){
            for(GuiTextField field : setting.textFields){
                field.mouseClicked(mouseX, mouseY, mouseButton);
            }
        } else {
            setting.textFields.add(new GuiTextField(0, x, y, 50, 9, "Resistance:", ColorUtil.WHITE, 69));
            setting.textFields.add(new GuiTextField(1, x, y, 50, 9, "Capacitance:", ColorUtil.WHITE, 69));
            setting.textFields.add(new GuiTextField(2, x, y, 50, 9, "Inductivity:", ColorUtil.WHITE, 69));
            setting.textFields.add(new GuiTextField(3, x, y, 50, 9, "Max. Voltage:", ColorUtil.WHITE, 69));
            setting.textFields.add(new GuiTextField(4, x, y, 50, 9, "Max. Current:", ColorUtil.WHITE, 69));
            setting.textFields.add(new GuiTextField(5, x, y, 50, 9, "Max. Power:", ColorUtil.WHITE, 69));
            setting.init();
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiScreen gui, GuiSetting setting, World world, EntityPlayer player, int guiLeft, int guiTop, float partialTicks, int mouseX, int mouseY){
        if(!setting.renderer.isEmpty()){
            setting.render(gui, this, guiLeft, guiTop, guiLeft + x, guiTop + y);
        }
    }

}
