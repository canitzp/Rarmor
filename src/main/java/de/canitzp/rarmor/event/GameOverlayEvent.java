package de.canitzp.rarmor.event;

import de.canitzp.rarmor.RarmorProperties;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IIngameTooltipHandler;
import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.items.rfarmor.ArmorHud;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.util.ColorUtil;
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
public class GameOverlayEvent {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent.Post event){
        if(event.type == RenderGameOverlayEvent.ElementType.ALL && MinecraftUtil.getCurrentScreen() == null) {
            EntityPlayer player = MinecraftUtil.getPlayer();
            FontRenderer fontRenderer = MinecraftUtil.getFontRenderer();
            ItemStack helmet = PlayerUtil.getArmor(player, EntityEquipmentSlot.HEAD);
            if(!RarmorProperties.getBoolean("AlwaysShowAdvancedInGameTooltip")){
                if(helmet != null && helmet.getItem() instanceof IIngameTooltipHandler){
                    ((IIngameTooltipHandler) helmet.getItem()).doRender(MinecraftUtil.getMinecraft(), player, event.resolution, fontRenderer, event.type, helmet, event.partialTicks);
                }
                if(RarmorUtil.isPlayerWearingRarmor(player)){
                    ItemStack module = NBTUtil.readSlots(PlayerUtil.getArmor(player, EntityEquipmentSlot.CHEST), ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
                    if(module != null && module.getItem() instanceof IRarmorModule){
                        ((IRarmorModule) module.getItem()).renderWorldScreen(MinecraftUtil.getMinecraft(), player, event.resolution, fontRenderer, event.type, module, event.partialTicks);
                    }
                }
            } else {
                ArmorHud.displayNames(MinecraftUtil.getMinecraft(), event.resolution, player, 5, ColorUtil.WHITE);
            }

        }
    }

}
