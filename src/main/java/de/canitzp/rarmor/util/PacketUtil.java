package de.canitzp.rarmor.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.CPacketCloseWindow;

/**
 * @author canitzp
 */
public class PacketUtil {

    public static void openPlayerInventoryFromClient(Minecraft minecraft, GuiScreen guiScreen){
        System.out.println("'openPlayerInventoryFromClient' isn't available in 1.9");
        guiScreen.onGuiClosed();
        minecraft.thePlayer.sendQueue.addToSendQueue(new CPacketCloseWindow(minecraft.thePlayer.openContainer.windowId));
        GuiInventory inventory = new GuiInventory(minecraft.thePlayer);
        minecraft.displayGuiScreen(inventory);
    }

}
