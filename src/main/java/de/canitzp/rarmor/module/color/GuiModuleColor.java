package de.canitzp.rarmor.module.color;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.inventory.ContainerRarmor;
import de.canitzp.rarmor.misc.Helper;
import de.canitzp.rarmor.packet.PacketHandler;
import de.canitzp.rarmor.packet.PacketSyncColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

/**
 * @author canitzp
 */
@OnlyIn(Dist.CLIENT)
public class GuiModuleColor extends RarmorModuleGui<ContainerRarmor>{

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("gui_rarmor_color");
    private int backHex = 0xFFFFFC30;

    private boolean isColorDirty = false;
    private int hex;
    private EquipmentSlotType activeSlot = EquipmentSlotType.CHEST;
    private Robot robot;

    public GuiModuleColor(ContainerScreen<ContainerRarmor> gui, ActiveRarmorModule module){
        super(gui, module);
        try{
            robot = new Robot();
        } catch(AWTException e){
            e.printStackTrace();
        }
        this.recolor();
    }
    
    @Override
    public void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY){
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
    
        this.mc.getTextureManager().bindTexture(RES_LOC);
        blit(matrixStack, this.guiLeft + 6, this.guiTop + 5, 0, 0, 250, 136);
    
        switch(this.activeSlot){
            case HEAD:{
                this.blit(matrixStack, this.guiLeft + 37, this.guiTop + 12, this.guiLeft + 125, this.guiTop + 47, backHex, backHex);
                break;
            }
            case CHEST:{
                this.blit(matrixStack, this.guiLeft + 37, this.guiTop + 48, this.guiLeft + 125, this.guiTop + 85, backHex, backHex);
                break;
            }
            case LEGS:{
                this.blit(matrixStack, this.guiLeft + 37, this.guiTop + 86, this.guiLeft + 125, this.guiTop + 110, backHex, backHex);
                break;
            }
            case FEET:{
                this.blit(matrixStack, this.guiLeft + 37, this.guiTop + 111, this.guiLeft + 125, this.guiTop + 140, backHex, backHex);
                break;
            }
        }
        InventoryScreen.drawEntityOnScreen(this.guiLeft+80, this.guiTop+128, 55, (float)(this.guiLeft+118)-mouseX, (float)(this.guiTop+45)-mouseY, this.mc.player);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY){
        if(mouseX - this.guiLeft >= 135 && mouseY - this.guiTop >= 32 && mouseX - this.guiLeft <= 227 && mouseY - this.guiTop <= 123){
            Point point = MouseInfo.getPointerInfo().getLocation();
            hex = robot.getPixelColor(point.x, point.y).getRGB();
            isColorDirty = true;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        if(mouseX - this.guiLeft >= 37 && mouseX - this.guiLeft <= 125){
            EquipmentSlotType slot = null;
            if(mouseY - this.guiTop >= 12 && mouseY - this.guiTop <= 47){
                slot = EquipmentSlotType.HEAD;
            } else if(mouseY - this.guiTop >= 48 && mouseY - this.guiTop <= 85){
                slot = EquipmentSlotType.CHEST;
            } else if(mouseY - this.guiTop >= 86 && mouseY - this.guiTop <= 110){
                slot = EquipmentSlotType.LEGS;
            } else if(mouseY - this.guiTop >= 111 && mouseY - this.guiTop <= 140){
                slot = EquipmentSlotType.FEET;
            }
            if(slot != null){
                if(button == 0){
                    this.activeSlot = slot;
                    this.recolor();
                } else if(button == 1){
                    copyColor(slot);
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void updateScreen(){
        if(isColorDirty && Minecraft.getInstance().world.getGameTime() % 3 == 0){
            ItemStack armor = Minecraft.getInstance().player.inventory.armorInventory.get(activeSlot.getIndex());
            CompoundNBT nbt = armor.hasTag() ? armor.getTag() : new CompoundNBT();
            nbt.putInt("Color", hex);
            armor.setTag(nbt);
            PacketHandler.channel.sendToServer(new PacketSyncColor(activeSlot));
            isColorDirty = false;
        }
        this.recolor();
        super.updateScreen();
    }

    private void setBackHex(int hex){
        this.backHex = ((0xFFFFFF - hex)) | (0xA6 << 24);
    }

    private void recolor(){
        ItemStack armor = Minecraft.getInstance().player.inventory.armorItemInSlot(activeSlot.getIndex());
        if(!armor.isEmpty() && armor.hasTag() && armor.getTag().contains("Color", 3)){
            this.hex = armor.getTag().getInt("Color");
        }
        this.setBackHex(this.hex);
    }

    private void copyColor(EquipmentSlotType from){
        createColorTag(from);
        ItemStack fromStack = Minecraft.getInstance().player.inventory.armorItemInSlot(from.getIndex());
        if(!fromStack.isEmpty() && fromStack.hasTag() && fromStack.getTag().contains("Color", 3)){
            createColorTag(activeSlot);
            ItemStack armor = Minecraft.getInstance().player.inventory.armorItemInSlot(activeSlot.getIndex());
            if(!armor.isEmpty()){
                this.hex = fromStack.getTag().getInt("Color");
                armor.getTag().putInt("Color", this.hex);
                this.isColorDirty = true;
            }
        }
    }

    private void createColorTag(EquipmentSlotType slot){
        ItemStack armor = Minecraft.getInstance().player.inventory.armorItemInSlot(slot.getIndex());
        if(!armor.isEmpty()){
            CompoundNBT nbt;
            if(!armor.hasTag()){
                armor.setTag(nbt = new CompoundNBT());
            } else {
                nbt = armor.getTag();
            }
            if(!nbt.contains("Color", 3)){
                nbt.putInt("Color", 0xFFFFFFFF);
            }
        }
    }

}
