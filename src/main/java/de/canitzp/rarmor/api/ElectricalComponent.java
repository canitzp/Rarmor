package de.canitzp.rarmor.api;

import com.google.common.collect.Lists;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.util.gui.GuiCheckBox;
import de.canitzp.util.util.ColorUtil;
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
    public int x, y, width, height, textureX, textureY;
    public String hoveringText;
    private List<GuiSetting> settings = Lists.newArrayList();
    private GuiCheckBox box;

    public ElectricalComponent(int x, int y, int width, int height, int textureX, int textureY, String hoveringText) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
    public void mouseClicked(GuiScreen gui, World world, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, int mouseButton) throws IOException {
        System.out.println("click");
        if(box != null){
            box.mouseClicked(mouseX, mouseY, guiLeft, guiTop);
        }else {
            box = new GuiCheckBox(gui, checkBox, x + 12, y + 2, 20, 8, "Ellpeck ist ein trottel", null, ColorUtil.WHITE);
            GuiSetting setting = new GuiSetting();
            setting.renderer.add(box);
            settings.add(setting);
            this.width += 100;
            this.height += 100;
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(GuiScreen gui, World world, EntityPlayer player, int guiLeft, int guiTop, float partialTicks, int mouseX, int mouseY){
        for(GuiSetting setting : settings){
            if(setting != null){
                setting.render(gui, guiLeft, guiTop, guiLeft + x, guiTop + y + 14, 100);
            }
        }
    }

}
