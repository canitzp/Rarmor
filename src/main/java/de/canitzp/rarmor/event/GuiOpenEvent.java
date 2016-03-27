package de.canitzp.rarmor.event;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.inventory.GuiHandler;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketOpenGui;
import de.canitzp.rarmor.network.PacketSendNBTBoolean;
import de.canitzp.rarmor.util.MinecraftUtil;
import de.canitzp.rarmor.util.NBTUtil;
import de.canitzp.rarmor.util.PlayerUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */

public class GuiOpenEvent {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void overrideGui(net.minecraftforge.client.event.GuiOpenEvent event){
        EntityPlayer player = MinecraftUtil.getPlayer();
        if(player != null){
            if(RarmorUtil.isPlayerWearingRarmor(player)){
                ItemStack body = PlayerUtil.getArmor(player, EntityEquipmentSlot.CHEST);
                if(event.getGui() instanceof GuiInventory && (body.getTagCompound() == null || !body.getTagCompound().getBoolean("click"))){
                    if(body.getTagCompound() != null) body.getTagCompound().setBoolean("click", false);
                    if(!NBTUtil.getBoolean(body, "isFirstOpened")){
                        NBTUtil.setBoolean(body, "isFirstOpened", true);
                        NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(player, 38, "isFirstOpened", true));
                    }
                    event.setCanceled(true);
                    NetworkHandler.wrapper.sendToServer(new PacketOpenGui(player,  GuiHandler.RFARMORGUI, Rarmor.instance));
                } else if(body.getTagCompound() != null) body.getTagCompound().setBoolean("click", false);
                if(RarmorUtil.isPlayerWearingRarmor(player)){
                    ItemStack module = NBTUtil.readSlots(PlayerUtil.getArmor(player, EntityEquipmentSlot.CHEST), ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
                    if(module != null && module.getItem() instanceof IRarmorModule){
                        ((IRarmorModule) module.getItem()).onGuiOpenEvent(player.worldObj, player, event.getGui(), module);
                    }
                }
            }
        }
    }

}
