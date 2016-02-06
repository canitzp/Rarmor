package de.canitzp.rarmor.event;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IIngameTooltipHandler;
import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author canitzp
 */
public class GameOverlayEvent {

    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent.Post event){
        Minecraft minecraft = Minecraft.getMinecraft();
        if(event.type == RenderGameOverlayEvent.ElementType.ALL && minecraft.currentScreen == null) {
            EntityPlayerSP player = minecraft.thePlayer;
            ItemStack helmet = player.getCurrentArmor(3);
            if(helmet != null && helmet.getItem() instanceof IIngameTooltipHandler){
                ((IIngameTooltipHandler) helmet.getItem()).doRender(minecraft, player, event.resolution, minecraft.fontRendererObj, event.type, helmet, event.partialTicks);
            }
            if(RarmorUtil.isPlayerWearingRarmor(player)){
                ItemStack module = NBTUtil.readSlots(player.getCurrentArmor(2), ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
                if(module != null && module.getItem() instanceof IRarmorModule){
                    ((IRarmorModule) module.getItem()).renderWorldScreen(minecraft, player, event.resolution, minecraft.fontRendererObj, event.type, module, event.partialTicks);
                }
            }
        }
    }

}
