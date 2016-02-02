package de.canitzp.rarmor.api;

import com.google.common.collect.Lists;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.inventory.gui.GuiText;
import de.canitzp.rarmor.inventory.gui.GuiTextField;
import de.canitzp.util.gui.GuiCheckBox;
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
public class ElectricalComponent implements IElectricalComponents, IGuiInteraction {

    public ResourceLocation location = new ResourceLocation(Rarmor.MODID, "textures/gui/guiElectricalComponents.png");
    public ResourceLocation checkBox = new ResourceLocation(Rarmor.MODID, "textures/gui/checkBox.png");
    public int x, y, width, height, textureX, textureY, textureW, textureH;
    public String hoveringText;
    private List<GuiSetting> settings = Lists.newArrayList();
    private GuiCheckBox box;
    private GuiText text;
    private GuiTextField field;

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
    public void mouseHover(GuiScreen gui, World world, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY){
        if(this.getX() + guiLeft <= mouseX && this.getX() + guiLeft + this.getTextureWidth() >= mouseX) {
            if (this.getY() + guiTop <= mouseY && this.getY() + guiTop + this.getTextureHeight() >= mouseY) {
                GuiUtil.drawHoveringText(gui, this.getHoveringText(), mouseX, mouseY, Minecraft.getMinecraft().fontRendererObj);
            }
        }
    }

    @Override
    public boolean onKeyPressed(GuiScreen gui, World world, EntityPlayer player, char typedChar, int keyCode) {
        return field != null && field.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(GuiScreen gui, World world, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, int mouseButton) throws IOException {
        if(box != null){
            box.mouseClicked(mouseX, mouseY, guiLeft, guiTop);
            field.mouseClicked(mouseX, mouseY, mouseButton);
        }else {
            box = new GuiCheckBox(gui, checkBox, x + 12, y + 2, 20, 8, "Ellpeck ist ein trottel", null, ColorUtil.WHITE);
            text = new GuiText("Resistance:", x + guiLeft + 12, y + guiTop + 12, ColorUtil.WHITE);
            field = new GuiTextField(0, x + guiLeft + 12, y + guiTop + 22, 140, 9);
            GuiSetting setting = new GuiSetting();
            setting.renderer.add(box);
            setting.renderer.add(text);
            setting.renderer.add(field);
            settings.add(setting);
            this.width += 150;
            this.height += setting.renderer.size() * 12;
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiScreen gui, World world, EntityPlayer player, int guiLeft, int guiTop, float partialTicks, int mouseX, int mouseY){
        for(GuiSetting setting : settings){
            if(setting != null){
                setting.render(gui, guiLeft, guiTop, guiLeft + x, guiTop + y + 14, 150);
            }
        }
    }

}
