/*
 * This file ("PacketOpenModule.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.packet;

import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenModule{

    private int moduleId;
    private boolean alsoSetData;
    private boolean sendRarmorDataToClient;

    public PacketOpenModule(){

    }

    public PacketOpenModule(int moduleId, boolean alsoSetData, boolean sendRarmorDataToClient){
        this.moduleId = moduleId;
        this.alsoSetData = alsoSetData;
        this.sendRarmorDataToClient = sendRarmorDataToClient;
    }
    
    public static PacketOpenModule fromBuffer(FriendlyByteBuf buf){
        PacketOpenModule pom = new PacketOpenModule();
        pom.moduleId = buf.readInt();
        pom.alsoSetData = buf.readBoolean();
        pom.sendRarmorDataToClient = buf.readBoolean();
        return pom;
    }

    public static void toBuffer(PacketOpenModule packet, FriendlyByteBuf buf){
        buf.writeInt(packet.moduleId);
        buf.writeBoolean(packet.alsoSetData);
        buf.writeBoolean(packet.sendRarmorDataToClient);
    }
    
    public static void handle(PacketOpenModule packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if(sender != null){
                RarmorAPI.methodHandler.openRarmor(sender, packet.moduleId, packet.alsoSetData, packet.sendRarmorDataToClient);
            }
        });
        ctx.get().setPacketHandled(true);
    }
    
}
