package de.canitzp.rarmor.network;

import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorGeneric;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorHelmet;
import net.minecraft.client.Minecraft;
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
            EntityPlayer player = minecraft.thePlayer;
            if(isRarmorEquipped(player)){
                ItemRFArmorHelmet item = (ItemRFArmorHelmet) player.getCurrentArmor(3).getItem();
                item.displayHud(minecraft, player, event.resolution);
            }
        }
    }

    private boolean isRarmorEquipped(EntityPlayer player) {
        ItemStack head = player.getCurrentArmor(3);
        ItemStack body = player.getCurrentArmor(2);
        ItemStack leggins = player.getCurrentArmor(1);
        ItemStack boots = player.getCurrentArmor(0);
        if (head != null && body != null && leggins != null && boots != null) {
            if (head.getItem() instanceof ItemRFArmorGeneric && body.getItem() instanceof ItemRFArmorBody && leggins.getItem() instanceof ItemRFArmorGeneric && boots.getItem() instanceof ItemRFArmorGeneric) {
                return true;
            }
        }
        return false;
    }
}
