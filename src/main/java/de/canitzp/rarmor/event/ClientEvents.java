/*
 * This file ("ClientEvents.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.event;

import de.canitzp.rarmor.CompatUtil;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.config.Config;
import de.canitzp.rarmor.inventory.gui.button.TexturedButton;
import de.canitzp.rarmor.misc.Helper;
import de.canitzp.rarmor.module.main.GuiModuleMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

@SideOnly(Side.CLIENT)
public class ClientEvents{

    public static boolean stopGuiOverride;
    private GuiButton invButton;

    public ClientEvents(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event){
        if(!stopGuiOverride){
            if(event.getGui() instanceof GuiInventory){
                EntityPlayer player = Minecraft.getMinecraft().player;
                int openingMode = Config.rarmorOpeningMode;
                boolean sneaking = player.isSneaking();
                if(openingMode == 2 || (openingMode == 0 && !sneaking) || (openingMode == 1 && sneaking)){
                    IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, true);
                    if(data != null){
                        this.openRarmor(player, data);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private void openRarmor(EntityPlayer player, IRarmorData data){
        RarmorAPI.methodHandler.openRarmorFromClient(data.getSelectedModule(), false, true);
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event){
        if(Config.showInventoryButton){
            GuiScreen gui = event.getGui();
            if(gui instanceof GuiInventory){
                int guiLeft = (gui.width-176)/2;
                int guiTop = (gui.height-166)/2;

                this.invButton = new TexturedButton(178223, guiLeft-20, guiTop+24, 20, 20, GuiModuleMain.RES_LOC, 0, 176);
                event.getButtonList().add(this.invButton);

                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(Minecraft.getMinecraft().player, true);
                this.invButton.visible = data != null;
            }
        }
    }

    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent event){
        if(this.invButton != null && this.invButton.visible){
            if(event.getGui() instanceof GuiInventory){
                if(event.getButton().id == this.invButton.id){
                    EntityPlayer player = Minecraft.getMinecraft().player;
                    IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, true);
                    if(data != null){
                        this.openRarmor(player, data);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post event){
        if(this.invButton != null){
            if(event.getGui() instanceof GuiInventory){
                if(this.invButton.visible && this.invButton.isMouseOver()){
                    Minecraft mc = Minecraft.getMinecraft();
                    GuiUtils.drawHoveringText(Collections.singletonList(I18n.format(RarmorAPI.MOD_ID+".open")), event.getMouseX(), event.getMouseY(), mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
                }

                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(Minecraft.getMinecraft().player, true);
                this.invButton.visible = data != null;
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event){
        if(event.getType() == ElementType.HOTBAR){
            Minecraft mc = Minecraft.getMinecraft();
            FontRenderer font = mc.fontRenderer;
            ScaledResolution res = event.getResolution();
            EntityPlayer player = mc.player;

            float scale = Config.rarmorOverlayScale;
            int renderX = Config.rarmorOverlayX;
            int renderY = Config.rarmorOverlayY;

            if(renderX >= 0 && renderY >= 0 && scale > 0){
                if(!mc.gameSettings.showDebugInfo){
                    if(player != null && player.getEntityWorld() != null){
                        ItemStack stack = RarmorAPI.methodHandler.getHasRarmorInSlot(player, EntityEquipmentSlot.CHEST);
                        if(!CompatUtil.isEmpty(stack)){
                            IRarmorData data = RarmorAPI.methodHandler.getDataForStack(player.getEntityWorld(), stack, false);
                            if(data != null){
                                GlStateManager.pushMatrix();
                                GlStateManager.scale(scale, scale, scale);

                                font.drawString(TextFormatting.GOLD+I18n.format(RarmorAPI.MOD_ID+".storedEnergy")+": ", renderX+20, renderY, 0xFFFFFF, true);
                                font.drawString(TextFormatting.YELLOW+""+data.getEnergyStored()+"/"+data.getMaxEnergyStored(), renderX+20, renderY+10, 0xFFFFFF, true);

                                Helper.renderStackToGui(stack, renderX, renderY, 1.0F);

                                if(!Config.rarmorOverlayOnlyEnergy){
                                    renderY += 26;

                                    for(ActiveRarmorModule module : data.getCurrentModules()){
                                        if(module.doesRenderOnOverlay(mc, player, data)){
                                            ItemStack display = module.getDisplayIcon();
                                            if(!CompatUtil.isEmpty(display)){
                                                Helper.renderStackToGui(display, renderX, renderY, 1.0F);
                                            }

                                            module.renderAdditionalOverlay(mc, player, data, res, renderX, renderY, event.getPartialTicks());
                                            renderY += 17;
                                        }
                                    }
                                }

                                GlStateManager.popMatrix();
                            }
                        }
                    }
                }
            }
        }
    }
}
