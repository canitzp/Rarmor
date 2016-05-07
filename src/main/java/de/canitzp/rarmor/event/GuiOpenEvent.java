/*
 * This file 'GuiOpenEvent.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.event;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.inventory.GuiHandler;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketOpenGui;
import de.canitzp.rarmor.network.PacketSendNBTBoolean;
import de.canitzp.rarmor.util.MinecraftUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
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

public class GuiOpenEvent{

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void overrideGui(net.minecraftforge.client.event.GuiOpenEvent event) {
        EntityPlayer player = MinecraftUtil.getPlayer();
        if (player != null) {
            if (RarmorUtil.isPlayerWearingRarmor(player)) {
                if (!NBTUtil.getBoolean(RarmorUtil.getPlayersRarmorChestplate(player), "HaveToSneakToOpenGui") || player.isSneaking()) {
                    ItemStack body = RarmorUtil.getArmor(player, EntityEquipmentSlot.CHEST);
                    if (event.getGui() instanceof GuiInventory && (body.getTagCompound() == null || !body.getTagCompound().getBoolean("click"))) {
                        if (body.getTagCompound() != null) body.getTagCompound().setBoolean("click", false);
                        if (!NBTUtil.getBoolean(body, "isFirstOpened")) {
                            NBTUtil.setBoolean(body, "isFirstOpened", true);
                            NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(player, 38, "isFirstOpened", true));
                        }
                        event.setCanceled(true);
                        NetworkHandler.wrapper.sendToServer(new PacketOpenGui(player, GuiHandler.RFARMORGUI));
                        ItemStack module = NBTUtil.readSlots(RarmorUtil.getArmor(player, EntityEquipmentSlot.CHEST), ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
                        if (module != null && module.getItem() instanceof IRarmorModule) {
                            ((IRarmorModule) module.getItem()).onGuiOpenEvent(player.worldObj, player, body, module, (GuiContainer) event.getGui());
                        }
                    } else if (body.getTagCompound() != null) body.getTagCompound().setBoolean("click", false);
                }
            }
        }
    }

}
