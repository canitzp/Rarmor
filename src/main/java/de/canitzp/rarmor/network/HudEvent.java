package de.canitzp.rarmor.network;

import de.canitzp.rarmor.api.IIngameTooltipHandler;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorGeneric;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorHelmet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author canitzp
 */
public class HudEvent {

    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent.Post event){
        Minecraft minecraft = Minecraft.getMinecraft();
        if(event.type == RenderGameOverlayEvent.ElementType.ALL && minecraft.currentScreen == null) {
            EntityPlayerSP player = minecraft.thePlayer;
            ItemStack helmet = player.getCurrentArmor(3);
            if(helmet != null && helmet.getItem() instanceof IIngameTooltipHandler){
                ((IIngameTooltipHandler) helmet.getItem()).doRender(minecraft, player, event.resolution, minecraft.fontRendererObj, event.type, helmet, event.partialTicks);
            }
            /*
            if(isRarmorEquipped(player)){
                ItemRFArmorHelmet item = (ItemRFArmorHelmet) player.getCurrentArmor(3).getItem();
                item.displayHud(minecraft, player, event.resolution);
            }
            */
        }
    }

}
