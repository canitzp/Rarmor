package de.canitzp.rarmor.module.color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.item.ItemRarmor;
import de.canitzp.rarmor.misc.Helper;
import de.canitzp.rarmor.packet.PacketHandler;
import de.canitzp.rarmor.packet.PacketSyncColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

/**
 * @author canitzp
 */
@OnlyIn(Dist.CLIENT)
public class GuiModuleColor extends RarmorModuleGui {

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("gui_rarmor_color");
    private int backHex = 0xFFFFFC30;

    private boolean isColorDirty = false;
    private int hex;
    private EquipmentSlot activeSlot = EquipmentSlot.CHEST;
    private Robot robot;

    public GuiModuleColor(ActiveRarmorModule module){
        super(module);
        try{
            robot = new Robot();
        } catch(AWTException e){
            e.printStackTrace();
        }
        this.recolor();
    }
    
    @Override
    public void drawGuiContainerBackgroundLayer(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY){
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, RES_LOC);
        blit(matrixStack, this.guiLeft + 6, this.guiTop + 5, 0, 0, 250, 136);

        switch(this.activeSlot){
            case HEAD:{
                this.fillGradient(matrixStack, this.guiLeft + 37, this.guiTop + 12, this.guiLeft + 125, this.guiTop + 47, backHex, backHex);
                break;
            }
            case CHEST:{
                this.fillGradient(matrixStack, this.guiLeft + 37, this.guiTop + 48, this.guiLeft + 125, this.guiTop + 85, backHex, backHex);
                break;
            }
            case LEGS:{
                this.fillGradient(matrixStack, this.guiLeft + 37, this.guiTop + 86, this.guiLeft + 125, this.guiTop + 110, backHex, backHex);
                break;
            }
            case FEET:{
                this.fillGradient(matrixStack, this.guiLeft + 37, this.guiTop + 111, this.guiLeft + 125, this.guiTop + 140, backHex, backHex);
                break;
            }
        }
        InventoryScreen.renderEntityInInventory(this.guiLeft+80, this.guiTop+128, 55, (float)(this.guiLeft+118)-mouseX, (float)(this.guiTop+45)-mouseY, Minecraft.getInstance().player);
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
            EquipmentSlot slot = null;
            if(mouseY - this.guiTop >= 12 && mouseY - this.guiTop <= 47){
                slot = EquipmentSlot.HEAD;
            } else if(mouseY - this.guiTop >= 48 && mouseY - this.guiTop <= 85){
                slot = EquipmentSlot.CHEST;
            } else if(mouseY - this.guiTop >= 86 && mouseY - this.guiTop <= 110){
                slot = EquipmentSlot.LEGS;
            } else if(mouseY - this.guiTop >= 111 && mouseY - this.guiTop <= 140){
                slot = EquipmentSlot.FEET;
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
        if(isColorDirty && Minecraft.getInstance().level.getGameTime() % 3 == 0){
            ItemStack armor = Minecraft.getInstance().player.getInventory().armor.get(activeSlot.getIndex());
            ItemRarmor.setArmorColor(armor, hex);
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
        this.hex = ItemRarmor.getArmorColor(Minecraft.getInstance().player.getInventory().getArmor(activeSlot.getIndex()));
        this.setBackHex(this.hex);
    }

    private void copyColor(EquipmentSlot from) {
        ItemStack fromStack = Minecraft.getInstance().player.getInventory().getArmor(from.getIndex());
        int color = ItemRarmor.getArmorColor(fromStack);
        ItemStack toStack = Minecraft.getInstance().player.getInventory().getArmor(activeSlot.getIndex());
        ItemRarmor.setArmorColor(toStack, color);
        this.isColorDirty = true;
    }

}
