package de.canitzp.api.util;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import de.canitzp.api.packets.PacketHandler;
import de.canitzp.api.packets.PacketOpenGui;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author canitzp
 */
public class PlayerUtil {

    public static void openInventoryFromServer(EntityPlayer player, Object modInstance, int guiId){
        //PacketUtil.sendPlayerCurrentSlotUpdate(player);
        FMLNetworkHandler.openGui(player, modInstance, guiId, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
    }

    public static void openInventoryFromClient(EntityPlayer player, Object modInstance, int guiId){
        PacketHandler.wrapper.sendToServer(new PacketOpenGui(player, guiId, modInstance));
    }

}
