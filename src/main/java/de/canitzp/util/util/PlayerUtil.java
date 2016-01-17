package de.canitzp.util.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * @author canitzp
 */
public class PlayerUtil {

    public static void openInventoryFromServer(EntityPlayer player, Object modInstance, int guiId){
        //PacketUtil.sendPlayerCurrentSlotUpdate(player);
        FMLNetworkHandler.openGui(player, modInstance, guiId, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
    }

}
