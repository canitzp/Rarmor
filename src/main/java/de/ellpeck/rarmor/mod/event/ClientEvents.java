/*
 * This file ("ClientEvents.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.event;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.mod.packet.PacketHandler;
import de.ellpeck.rarmor.mod.packet.PacketOpenModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEvents{

    public static boolean stopGuiOverride;

    public ClientEvents(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event){
        if(!stopGuiOverride){
            if(event.getGui() instanceof GuiInventory){
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                if(!player.isSneaking()){
                    IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, true);
                    if(data != null){
                        PacketHandler.handler.sendToServer(new PacketOpenModule(data.getSelectedModule(), false, true));
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
