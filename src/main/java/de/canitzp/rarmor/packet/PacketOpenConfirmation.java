/*
 * This file ("PacketOpenConfirmation.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.packet;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.inventory.ContainerRarmor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenConfirmation {

    private int moduleId;

    public PacketOpenConfirmation(){}
    
    public PacketOpenConfirmation(PacketBuffer buf){
        this.moduleId = buf.readInt();
    }

    public PacketOpenConfirmation(int moduleId){
        this.moduleId = moduleId;
    }

    public static void toBuffer(PacketOpenConfirmation packet, PacketBuffer buf){
        buf.writeInt(packet.moduleId);
    }
    
    public static void handle(PacketOpenConfirmation packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            if(sender != null){
                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(sender, true);
                ActiveRarmorModule activeRarmorModule = data.getCurrentModules().get(data.getCurrentModules().size() <= data.getSelectedModule() ? 0 : data.getSelectedModule());
                sender.openContainer(new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName(){
                        return new StringTextComponent("");
                    }
    
                    @Override
                    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player){
                        return new ContainerRarmor(windowId, player, activeRarmorModule);
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
