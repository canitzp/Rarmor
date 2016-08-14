/*
 * This file ("PacketSyncRarmorData.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.packet;

import de.canitzp.rarmor.mod.data.RarmorData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketSyncRarmorData implements IMessage{

    private UUID uuid;
    private RarmorData data;

    public PacketSyncRarmorData(){

    }

    public PacketSyncRarmorData(RarmorData data){

    }

    @Override
    public void fromBytes(ByteBuf buf){

    }

    @Override
    public void toBytes(ByteBuf buf){

    }

    public static class Handler implements IMessageHandler<PacketSyncRarmorData, IMessage>{

        @Override
        public IMessage onMessage(PacketSyncRarmorData message, MessageContext context){
            EntityPlayerMP player = context.getServerHandler().playerEntity;
            return null;
        }
    }
}
