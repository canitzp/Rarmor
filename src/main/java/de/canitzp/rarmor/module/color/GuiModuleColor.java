package de.canitzp.rarmor.module.color;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.misc.Helper;
import de.canitzp.rarmor.packet.PacketHandler;
import de.canitzp.rarmor.packet.PacketSyncColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.io.IOException;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiModuleColor extends RarmorModuleGui{

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("gui_rarmor_color");
    private int backHex = 0xFFFFFC30;

    private boolean isColorDirty = false;
    private int hex;
    private EntityEquipmentSlot activeSlot = EntityEquipmentSlot.CHEST;
    private Robot robot;

    public GuiModuleColor(GuiContainer gui, ActiveRarmorModule module){
        super(gui, module);
        try{
            robot = new Robot();
        } catch(AWTException e){
            e.printStackTrace();
        }
        this.recolor();
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.drawTexturedModalRect(this.guiLeft+6, this.guiTop+5, 0, 0, 250, 136);

        switch(activeSlot){
            case HEAD:{
                this.drawGradientRect(this.guiLeft + 37, this.guiTop + 12, this.guiLeft + 125, this.guiTop + 47, backHex, backHex);
                break;
            }
            case CHEST:{
                this.drawGradientRect(this.guiLeft + 37, this.guiTop + 48, this.guiLeft + 125, this.guiTop + 85, backHex, backHex);
                break;
            }
            case LEGS:{
                this.drawGradientRect(this.guiLeft + 37, this.guiTop + 86, this.guiLeft + 125, this.guiTop + 110, backHex, backHex);
                break;
            }
            case FEET:{
                this.drawGradientRect(this.guiLeft + 37, this.guiTop + 111, this.guiLeft + 125, this.guiTop + 140, backHex, backHex);
                break;
            }
        }

        GuiInventory.drawEntityOnScreen(this.guiLeft+80, this.guiTop+128, 55, (float)(this.guiLeft+118)-mouseX, (float)(this.guiTop+45)-mouseY, this.mc.player);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        if(mouseX >= 135 && mouseY >= 32 && mouseX <= 227 && mouseY <= 123){
            Point point = MouseInfo.getPointerInfo().getLocation();
            hex = robot.getPixelColor(point.x, point.y).getRGB();
            isColorDirty = true;
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        if(mouseX >= 37 && mouseX <= 125){
            EntityEquipmentSlot slot = null;
            if(mouseY >= 12 && mouseY <= 47){
                slot = EntityEquipmentSlot.HEAD;
            } else if(mouseY >= 48 && mouseY <= 85){
                slot = EntityEquipmentSlot.CHEST;
            } else if(mouseY >= 86 && mouseY <= 110){
                slot = EntityEquipmentSlot.LEGS;
            } else if(mouseY >= 111 && mouseY <= 140){
                slot = EntityEquipmentSlot.FEET;
            }
            if(slot != null){
                if(mouseButton == 0){
                    this.activeSlot = slot;
                    this.recolor();
                } else if(mouseButton == 1){
                    copyColor(slot);
                }
            }
        }
    }

    @Override
    public void updateScreen(){
        if(isColorDirty && Minecraft.getMinecraft().world.getTotalWorldTime() % 3 == 0){
            ItemStack armor = Minecraft.getMinecraft().player.inventory.armorInventory.get(activeSlot.getIndex());
            NBTTagCompound nbt = armor.hasTagCompound() ? armor.getTagCompound() : new NBTTagCompound();
            nbt.setInteger("Color", hex);
            armor.setTagCompound(nbt);
            PacketHandler.handler.sendToServer(new PacketSyncColor(activeSlot));
            isColorDirty = false;
        }
        this.recolor();
        super.updateScreen();
    }

    private void setBackHex(int hex){
        this.backHex = ((0xFFFFFF - hex)) | (0xA6 << 24);
    }

    private void recolor(){
        ItemStack armor = Minecraft.getMinecraft().player.inventory.armorItemInSlot(activeSlot.getIndex());
        if(!armor.isEmpty() && armor.hasTagCompound() && armor.getTagCompound().hasKey("Color", 3)){
            this.hex = armor.getTagCompound().getInteger("Color");
        }
        this.setBackHex(this.hex);
    }

    private void copyColor(EntityEquipmentSlot from){
        createColorTag(from);
        ItemStack fromStack = Minecraft.getMinecraft().player.inventory.armorItemInSlot(from.getIndex());
        if(!fromStack.isEmpty() && fromStack.hasTagCompound() && fromStack.getTagCompound().hasKey("Color", 3)){
            createColorTag(activeSlot);
            ItemStack armor = Minecraft.getMinecraft().player.inventory.armorItemInSlot(activeSlot.getIndex());
            if(!armor.isEmpty()){
                this.hex = fromStack.getTagCompound().getInteger("Color");
                armor.getTagCompound().setInteger("Color", this.hex);
                this.isColorDirty = true;
            }
        }
    }

    private void createColorTag(EntityEquipmentSlot slot){
        ItemStack armor = Minecraft.getMinecraft().player.inventory.armorItemInSlot(slot.getIndex());
        if(!armor.isEmpty()){
            NBTTagCompound nbt;
            if(!armor.hasTagCompound()){
                armor.setTagCompound(nbt = new NBTTagCompound());
            } else {
                nbt = armor.getTagCompound();
            }
            if(!nbt.hasKey("Color", 3)){
                nbt.setInteger("Color", 0xFFFFFFFF);
            }
        }
    }

}
