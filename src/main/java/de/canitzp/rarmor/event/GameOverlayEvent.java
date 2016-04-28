/*
 * This file 'GameOverlayEvent.java' is part of Rarmor by canitzp.
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
import de.canitzp.rarmor.util.PlayerUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class GameOverlayEvent{

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent.Post event){
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL && MinecraftUtil.getCurrentScreen() == null){
            EntityPlayer player = MinecraftUtil.getPlayer();
            FontRenderer fontRenderer = MinecraftUtil.getFontRenderer();
            ItemStack helmet = PlayerUtil.getArmor(player, EntityEquipmentSlot.HEAD);
            if(!RarmorProperties.getBoolean("AlwaysShowAdvancedInGameTooltip")){
                if(helmet != null && helmet.getItem() instanceof IIngameTooltipHandler){
                    ((IIngameTooltipHandler) helmet.getItem()).doRender(MinecraftUtil.getMinecraft(), player, event.getResolution(), fontRenderer, event.getType(), helmet, event.getPartialTicks());
                }
                if(RarmorUtil.isPlayerWearingRarmor(player)){
                    ItemStack module = NBTUtil.readSlots(PlayerUtil.getArmor(player, EntityEquipmentSlot.CHEST), ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
                    if(module != null && module.getItem() instanceof IRarmorModule){
                        ((IRarmorModule) module.getItem()).renderWorldScreen(MinecraftUtil.getMinecraft(), player, event.getResolution(), fontRenderer, event.getType(), RarmorUtil.getPlayersRarmorChestplate(player), module, event.getPartialTicks());
                    }
                }
            } else {
                ArmorHud.display(MinecraftUtil.getMinecraft(), event.getResolution(), player, 0, 5);
            }

        }
    }

}
