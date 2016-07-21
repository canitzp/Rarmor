package de.canitzp.rarmor;

import de.canitzp.rarmor.api.RarmorValues;
import de.canitzp.rarmor.api.tooltip.TooltipComponent;
import de.canitzp.rarmor.network.PacketAddToPlayerNBT;
import de.canitzp.rarmor.network.PacketHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Collections;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiIWTSettings extends GuiScreen {

    private static final String EXAMPLE_TEXT_1 = "This is a test line";
    private static final String EXAMPLE_TEXT_2 = "And this the second";
    public static final String DATA_NAME = "RarmorTooltipData";
    private EntityPlayer player;
    private boolean flag;
    private int lastMouseX, lastMouseY;

    public static int offsetX = -1, offsetY = -1, textColor = -1;
    public static float scale = -1;

    public static void setValues(EntityPlayer player, boolean force){
        if(force || (offsetX == -1 && offsetY == -1 && textColor == -1 && scale == -1)){
            NBTTagCompound nbt = player.getEntityData().getCompoundTag(DATA_NAME);
            if(nbt.hasKey("OffsetX") && nbt.hasKey("OffsetY") && nbt.hasKey("TextColor") && nbt.hasKey("Scale")){
                offsetX = nbt.getInteger("OffsetX");
                offsetY = nbt.getInteger("OffsetY");
                textColor = nbt.getInteger("TextColor");
                scale = nbt.getFloat("Scale");
            } else {
                offsetX = MathHelper.floor_float(RarmorValues.defaultValues[0]);
                offsetY = MathHelper.floor_float(RarmorValues.defaultValues[1]);
                textColor = MathHelper.floor_float(RarmorValues.defaultValues[2]);
                scale = RarmorValues.defaultValues[3];
            }
        }
    }

    public GuiIWTSettings(EntityPlayer player){
        this.player = player;
        setValues(player, true);
        this.flag = RarmorValues.tooltipsAlwaysActive;
        RarmorValues.tooltipsAlwaysActive = true;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        InWorldTooltips.showList(this.fontRendererObj, this.width, offsetY, Collections.singletonList(new TooltipComponent().addText(EXAMPLE_TEXT_1).newLine().addText(EXAMPLE_TEXT_2)));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(isMouseOverTooltip(getX(), offsetY, getWidth(), getHeight(), mouseX, mouseY)){
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if(isMouseOverTooltip(getX(), offsetY, getWidth(), getHeight(), mouseX, mouseY)){
            offsetX = offsetX + (mouseX - this.lastMouseX);
            offsetY = offsetY + (mouseY - this.lastMouseY);
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }
    }

    private boolean isMouseOverTooltip(int x, int y, int width, int height, int mouseX, int mouseY){
        if(mouseX >= x && mouseY >= y){
            if(mouseX <= x + width && mouseY <= y + height){
                return true;
            }
        }
        return false;
    }

    private int getX(){
        return this.width/2-getWidth()/2 + offsetX;
    }

    private int getWidth(){
        int ext1l = this.fontRendererObj.getStringWidth(EXAMPLE_TEXT_1);
        int ext2l = this.fontRendererObj.getStringWidth(EXAMPLE_TEXT_2);
        return ext1l >= ext2l ? ext1l : ext2l;
    }

    private int getHeight(){
        return 22;
    }

    @Override
    public void onGuiClosed() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("OffsetX", offsetX);
        nbt.setInteger("OffsetY", offsetY);
        nbt.setInteger("TextColor", textColor);
        nbt.setFloat("Scale", scale);
        this.player.getEntityData().setTag(DATA_NAME, nbt);
        PacketHandler.network.sendToServer(new PacketAddToPlayerNBT(this.player, nbt));
        RarmorValues.tooltipsAlwaysActive = this.flag;
        super.onGuiClosed();
    }
}
