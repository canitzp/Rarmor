/*
 * This file ("ClientEvents.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.event;

import com.mojang.blaze3d.platform.GlStateManager;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.config.Config;
import de.canitzp.rarmor.inventory.gui.button.TexturedButton;
import de.canitzp.rarmor.module.main.GuiModuleMain;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RarmorAPI.MOD_ID)
@OnlyIn(Dist.CLIENT)
public class ClientEvents {
    
    public static boolean stopGuiOverride;
    private static Button invButton;
    
    @SubscribeEvent
    public static void onOpenGui(GuiOpenEvent event){
        if(!stopGuiOverride){
            if(event.getGui() instanceof InventoryScreen){
                PlayerEntity player = Minecraft.getInstance().player;
                int openingMode = Config.GENERAL.INVENTORY_OPENING_MODE.get();
                boolean sneaking = player.isSneaking();
                if(openingMode == 2 || (openingMode == 0 && !sneaking) || (openingMode == 1 && sneaking)){
                    IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, true);
                    if(data != null){
                        openRarmor(player, data);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
    
    private static void openRarmor(PlayerEntity player, IRarmorData data){
        RarmorAPI.methodHandler.openRarmorFromClient(data.getSelectedModule(), false, true);
    }
    
    @SubscribeEvent
    public static void onInitGui(GuiScreenEvent.InitGuiEvent.Post event){
        if(Config.GENERAL.SHOW_INVENTORY_BUTTON.get()){
            Screen gui = event.getGui();
            if(gui instanceof InventoryScreen){
                int guiLeft = (gui.width - 176) / 2;
                int guiTop = (gui.height - 166) / 2;
    
                invButton = new TexturedButton(178223, guiLeft - 20, guiTop + 24, 20, 20, GuiModuleMain.RES_LOC, 0, 176);
                event.getButtonList().add(invButton);
    
                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(Minecraft.getInstance().player, true);
                invButton.visible = data != null;
            }
        }
    }
    
    @SubscribeEvent
    public static void onActionPerformed(GuiScreenEvent.ActionPerformedEvent event){
        if(invButton != null && invButton.visible){
            if(event.getGui() instanceof InventoryScreen){
                if(event.getButton() == invButton){
                    PlayerEntity player = Minecraft.getInstance().player;
                    IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, true);
                    if(data != null){
                        openRarmor(player, data);
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post event){
        if(invButton != null){
            if(event.getGui() instanceof InventoryScreen){
                if(invButton.visible && invButton.isMouseOver()){
                    Minecraft mc = Minecraft.getInstance();
                    GuiUtils.drawHoveringText(Collections.singletonList(I18n.format(RarmorAPI.MOD_ID + ".open")), event.getMouseX(), event.getMouseY(), mc.mainWindow.getWidth(), mc.mainWindow.getHeight(), -1, mc.fontRenderer);
                }
    
                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(Minecraft.getInstance().player, true);
                invButton.visible = data != null;
            }
        }
    }
    
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event){
        if(event.getType() == ElementType.HOTBAR){
            Minecraft mc = Minecraft.getInstance();
            FontRenderer font = mc.fontRenderer;
            MainWindow mainWindow = mc.mainWindow;
            PlayerEntity player = mc.player;
    
            double scale = Config.GENERAL.OVERLAY_SCALE.get();
            int renderX = Config.GENERAL.OVERLAY_X.get();
            int renderY = Config.GENERAL.OVERLAY_Y.get();
    
            if(renderX >= 0 && renderY >= 0 && scale > 0){
                if(!mc.gameSettings.showDebugInfo){
                    if(player != null && player.getEntityWorld() != null){
                        ItemStack stack = RarmorAPI.methodHandler.getHasRarmorInSlot(player, EquipmentSlotType.CHEST);
                        if(!stack.isEmpty()){
                            IRarmorData data = RarmorAPI.methodHandler.getDataForStack(player.getEntityWorld(), stack, false);
                            if(data != null){
                                GlStateManager.pushMatrix();
                                GlStateManager.scaled(scale, scale, scale);
    
                                font.drawStringWithShadow(TextFormatting.GOLD + I18n.format(RarmorAPI.MOD_ID + ".storedEnergy") + ": ", renderX + 20, renderY, 0xFFFFFF);
                                font.drawStringWithShadow(TextFormatting.YELLOW + "" + data.getEnergyStored() + "/" + data.getMaxEnergyStored(), renderX + 20, renderY + 10, 0xFFFFFF);
    
                                Helper.renderStackToGui(stack, renderX, renderY, 1.0F);
    
                                if(!Config.GENERAL.OVERLAY_ENERGY_ONLY.get()){
                                    renderY += 26;
        
                                    for(ActiveRarmorModule module : data.getCurrentModules()){
                                        if(module.doesRenderOnOverlay(mc, player, data)){
                                            ItemStack display = module.getDisplayIcon();
                                            if(!display.isEmpty()){
                                                Helper.renderStackToGui(display, renderX, renderY, 1.0F);
                                            }
    
                                            module.renderAdditionalOverlay(mc, player, data, mainWindow, renderX, renderY, event.getPartialTicks());
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