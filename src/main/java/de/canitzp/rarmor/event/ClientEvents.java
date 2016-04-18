package de.canitzp.rarmor.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class ClientEvents {

    public ClientEvents() {
    }

    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent event){
        EntityPlayer player = event.getEntityPlayer();
        if(player.getName().equals("canitzp")){
            System.out.println("render");
        }

    }

}
