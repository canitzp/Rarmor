/*
 * This file ("ClientEvents.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.event;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.misc.Config;
import de.ellpeck.rarmor.mod.misc.Helper;
import de.ellpeck.rarmor.mod.packet.PacketHandler;
import de.ellpeck.rarmor.mod.packet.PacketOpenModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEvents{

    public static boolean stopGuiOverride;

    private static boolean hasAlreadyAnnoyedThePlayerAboutTheFactThatThisIsNotAFinishedProduct;

    public ClientEvents(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event){
        if(!stopGuiOverride){
            if(event.getGui() instanceof GuiInventory){
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                if(!player.isSneaking()){
                    IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, true);
                    if(data != null){
                        PacketHandler.handler.sendToServer(new PacketOpenModule(data.getSelectedModule(), false, true));
                        event.setCanceled(true);

                        if(!hasAlreadyAnnoyedThePlayerAboutTheFactThatThisIsNotAFinishedProduct){
                            player.addChatComponentMessage(new TextComponentTranslation(RarmorAPI.MOD_ID+".notFinishedInfo.1", TextFormatting.GREEN+"github.com/Ellpeck/Rarmor"+TextFormatting.RESET).setStyle(new Style().setColor(TextFormatting.RED)));
                            player.addChatComponentMessage(new TextComponentTranslation(RarmorAPI.MOD_ID+".notFinishedInfo.2").setStyle(new Style().setColor(TextFormatting.GOLD)));
                            hasAlreadyAnnoyedThePlayerAboutTheFactThatThisIsNotAFinishedProduct = true;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event){
        if(event.getType() == ElementType.HOTBAR){
            Minecraft mc = Minecraft.getMinecraft();
            FontRenderer font = mc.fontRendererObj;
            ScaledResolution res = event.getResolution();
            EntityPlayer player = mc.thePlayer;

            if(player != null && player.worldObj != null){
                ItemStack stack = RarmorAPI.methodHandler.getHasRarmorInSlot(player, EntityEquipmentSlot.CHEST);
                if(stack != null){
                    IRarmorData data = RarmorAPI.methodHandler.getDataForStack(player.worldObj, stack, false);
                    if(data != null){
                        float scale = Config.rarmorOverlayScale;
                        int renderX = Config.rarmorOverlayX;
                        int renderY = Config.rarmorOverlayY;

                        GlStateManager.pushMatrix();
                        GlStateManager.scale(scale, scale, scale);

                        font.drawString(data.getEnergyStored()+"/"+data.getMaxEnergyStored()+" RF", renderX+18, renderY+5, 0xFFFFFF, true);
                        Helper.renderStackToGui(stack, renderX, renderY, 1.0F);

                        renderY += 22;

                        for(ActiveRarmorModule module : data.getCurrentModules()){
                            ItemStack display = module.getDisplayIcon();
                            if(display != null){
                                Helper.renderStackToGui(display, renderX, renderY, 1.0F);
                            }

                            module.renderAdditionalOverlay(mc, player, data, res, renderX, renderY, event.getPartialTicks());
                            renderY += 17;
                        }

                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }
}
