package de.canitzp.rarmor.api.gui;

import de.canitzp.rarmor.util.MinecraftUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author canitzp
 */
public class GuiTextField extends net.minecraft.client.gui.GuiTextField implements IGuiRender{

    public String text;
    public int color, end;
    private FontRenderer fontRenderer = MinecraftUtil.getFontRenderer();

    public GuiTextField(int componentId, int x, int y, int width, int height){
        this(componentId, x, y, width, height, "", 0);
    }

    public GuiTextField(int componentId, int x, int y, int width, int height, String text, int color){
        this(componentId, x, y, width, height, text, color, 50);
    }

    public GuiTextField(int componentId, int x, int y, int width, int height, String text, int color, int end){
        super(componentId, MinecraftUtil.getFontRenderer(), x, y, width, height);
        this.text = text;
        this.color = color;
        this.end = end;
    }

    @Override
    public void render(GuiScreen gui, int guiLeft, int guiTop, int x, int y){
        if (this.text != null){
            fontRenderer.drawString(this.text, x, y, color);
        }
        this.xPosition = x + end;
        this.yPosition = y;
        this.drawTextBox();
    }

    @Override
    public int getLength(FontRenderer fontRenderer){
        int i = 0;
        if (this.text != null){
            i += fontRenderer.getStringWidth(this.text) + 3;
        }
        return i + this.width;
    }

    public NBTTagCompound writeNBT(NBTTagCompound compound){
        compound.setString("SavedText" + this.text, this.getText());
        return compound;
    }
}
