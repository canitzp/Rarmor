/*
 * This file 'ClientEvents.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.event;

import de.canitzp.rarmor.RarmorProperties;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IIngameTooltipHandler;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.items.rfarmor.ArmorHud;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.util.MinecraftUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class ClientEvents{

    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent.Post event){
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL && MinecraftUtil.getCurrentScreen() == null){
            EntityPlayer player = MinecraftUtil.getPlayer();
            FontRenderer fontRenderer = MinecraftUtil.getFontRenderer();
            ItemStack helmet = RarmorUtil.getArmor(player, EntityEquipmentSlot.HEAD);
            if(!RarmorProperties.getBoolean("AlwaysShowAdvancedInGameTooltip")){
                if(helmet != null && helmet.getItem() instanceof IIngameTooltipHandler){
                    ((IIngameTooltipHandler) helmet.getItem()).doRender(MinecraftUtil.getMinecraft(), player, event.getResolution(), fontRenderer, event.getType(), helmet, event.getPartialTicks());
                }
                if(RarmorUtil.isPlayerWearingRarmor(player)){
                    ItemStack module = NBTUtil.readSlots(RarmorUtil.getArmor(player, EntityEquipmentSlot.CHEST), ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
                    if(module != null && module.getItem() instanceof IRarmorModule){
                        ((IRarmorModule) module.getItem()).renderWorldScreen(MinecraftUtil.getMinecraft(), player, RarmorUtil.getPlayersRarmorChestplate(player), module, event.getResolution(), fontRenderer, event.getType(), event.getPartialTicks());
                    }
                }
            } else {
                ArmorHud.display(MinecraftUtil.getMinecraft(), event.getResolution(), player, 0, 5);
            }

        }
    }

    //@SubscribeEvent
    public void onGameRenderEvent(RenderPlayerEvent.Post event){
        EntityPlayer player = event.getEntityPlayer();
        if(!player.isPlayerSleeping()){
            GlStateManager.pushMatrix();

            GlStateManager.rotate(-player.renderYawOffset, 0, 1, 0);
            GlStateManager.translate(-0.19, 0.97, -0.2);

            GlStateManager.scale(0.12, 0.12, 0.12);
            if(player.isSneaking()){
                GlStateManager.rotate(25, 1, 0, 0);
                GlStateManager.translate(0, -1.25, -1.25);
            }
            renderLetter(new ItemStack(Blocks.WOOL, 1, 11), Letters.X);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    public void renderLetter(ItemStack stack, Letters letter){
        for(int[] ints : letter.letterArray){
            for(int i : ints){
                GlStateManager.translate(0.5, 0, 0);
                if(i == 1){
                    Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
                }
            }
            GlStateManager.translate(-2.5, 0.5, 0);
        }
    }

    enum Letters{
        C(new int[][]{{1,1,1,1,1}, {0,0,0,0,1}, {0,0,0,0,1}, {0,0,0,0,1}, {0,0,0,0,1}, {1,1,1,1,1}}),
        X(new int[][]{{1,0,0,0,1}, {1,0,0,0,1}, {0,1,0,1,0}, {0,0,1,0,0}, {0,1,0,1,0}, {1,0,0,0,1}}),
        Z(new int[][]{{1,1,1,1,1}, {0,0,0,0,1}, {0,0,0,1,0}, {0,0,1,0,0}, {0,1,0,0,0}, {1,1,1,1,1}}),
        ;

        public int[][] letterArray;
        Letters(int[][] ints){
            this.letterArray = ints;
        }
    }

}
