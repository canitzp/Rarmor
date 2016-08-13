/*
 * This file ("PacketOpenGui.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.packet;

import de.canitzp.rarmor.mod.Rarmor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenGui implements IMessage{

    public int guiId;

    public PacketOpenGui(){

    }

    public PacketOpenGui(int guiId){
        this.guiId = guiId;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        this.guiId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeInt(this.guiId);
    }

    public static class Handler implements IMessageHandler<PacketOpenGui, IMessage>{

        @Override
        public IMessage onMessage(PacketOpenGui message, MessageContext context){
            EntityPlayerMP player = context.getServerHandler().playerEntity;
            player.openGui(Rarmor.instance, message.guiId, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            return null;
        }
    }
}
