package de.canitzp.rarmor.network;

import de.canitzp.util.packets.PacketOpenGui;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.inventory.GuiHandler;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorGeneric;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author canitzp
 */
public class GuiOpenChanger {

    @SubscribeEvent
    public void overrideGui(GuiOpenEvent event){
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player != null){
            ItemStack head = player.getCurrentArmor(3);
            ItemStack body = player.getCurrentArmor(2);
            ItemStack leggins = player.getCurrentArmor(1);
            ItemStack boots = player.getCurrentArmor(0);
            if(head != null && body != null && leggins != null && boots != null){
                if(head.getItem() instanceof ItemRFArmorGeneric && body.getItem() instanceof ItemRFArmorBody && leggins.getItem() instanceof ItemRFArmorGeneric && boots.getItem() instanceof ItemRFArmorGeneric){
                    if(event.gui instanceof GuiInventory && (body.getTagCompound() == null || !body.getTagCompound().getBoolean("click"))){
                        if(body.getTagCompound() != null) body.getTagCompound().setBoolean("click", false);
                        if(!NBTUtil.getBoolean(body, "isFirstOpened")){
                            NBTUtil.setBoolean(body, "isFirstOpened", true);
                            NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(player, 38, "isFirstOpened", true));
                        }
                        event.setCanceled(true);
                        NetworkHandler.wrapper.sendToServer(new PacketOpenGui(player,  GuiHandler.RFArmorGui, Rarmor.instance));
                    } else if(body.getTagCompound() != null) body.getTagCompound().setBoolean("click", false);
                }
            }
        }
    }

}
