package de.canitzp.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;

/**
 * @author canitzp
 */
public class PacketUtil {

    public static void openPlayerInventoryFromClient(Minecraft minecraft, GuiScreen guiScreen){
        guiScreen.onGuiClosed();
        minecraft.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(minecraft.thePlayer.openContainer.windowId));
        GuiInventory inventory = new GuiInventory(minecraft.thePlayer);
        minecraft.displayGuiScreen(inventory);
    }

    public static void sendPlayerCurrentSlotUpdate(EntityPlayer player){
        if(player instanceof EntityPlayerMP){
            if(player.getHeldItem() != null){
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, player.inventory.currentItem, player.getHeldItem()));
            }
        }
    }

}
