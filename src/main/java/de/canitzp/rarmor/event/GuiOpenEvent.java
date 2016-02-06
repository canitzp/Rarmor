package de.canitzp.rarmor.event;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.inventory.GuiHandler;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorGeneric;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketSendNBTBoolean;
import de.canitzp.util.packets.PacketOpenGui;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author canitzp
 */
public class GuiOpenEvent {

    @SubscribeEvent
    public void overrideGui(net.minecraftforge.client.event.GuiOpenEvent event){
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player != null){
            if(RarmorUtil.isPlayerWearingRarmor(player)){
                ItemStack body = player.getCurrentArmor(2);
                if(event.gui instanceof GuiInventory && (body.getTagCompound() == null || !body.getTagCompound().getBoolean("click"))){
                    if(body.getTagCompound() != null) body.getTagCompound().setBoolean("click", false);
                    if(!NBTUtil.getBoolean(body, "isFirstOpened")){
                        NBTUtil.setBoolean(body, "isFirstOpened", true);
                        NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(player, 38, "isFirstOpened", true));
                    }
                    event.setCanceled(true);
                    NetworkHandler.wrapper.sendToServer(new PacketOpenGui(player,  GuiHandler.RFARMORGUI, Rarmor.instance));
                } else if(body.getTagCompound() != null) body.getTagCompound().setBoolean("click", false);
                if(RarmorUtil.isPlayerWearingRarmor(player)){
                    ItemStack module = NBTUtil.readSlots(player.getCurrentArmor(2), ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
                    if(module != null && module.getItem() instanceof IRarmorModule){
                        ((IRarmorModule) module.getItem()).onGuiOpenEvent(player.worldObj, (EntityPlayerSP) player, event.gui, module);
                    }
                }
            }
        }
    }

}
