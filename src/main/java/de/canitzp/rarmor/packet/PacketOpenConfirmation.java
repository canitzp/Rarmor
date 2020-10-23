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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenConfirmation {

    private final int moduleId;
    
    public PacketOpenConfirmation(int moduleId){
        this.moduleId = moduleId;
    }

    public PacketOpenConfirmation(PacketBuffer buf){
        this.moduleId = buf.readInt();
    }

    public static void toBuffer(PacketOpenConfirmation packet, PacketBuffer buf){
        buf.writeInt(packet.moduleId);
    }
    
    public static void handle(PacketOpenConfirmation packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            if(sender != null){
                RarmorAPI.methodHandler.openRarmor(sender, packet.moduleId, true, true);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
