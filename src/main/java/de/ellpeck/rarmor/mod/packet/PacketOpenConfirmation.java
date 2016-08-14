/*
 * This file ("PacketOpenConfirmation.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.packet;

import de.ellpeck.rarmor.mod.Rarmor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenConfirmation implements IMessage{

    private int moduleId;

    public PacketOpenConfirmation(){

    }

    public PacketOpenConfirmation(int moduleId){
        this.moduleId = moduleId;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        this.moduleId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeInt(this.moduleId);
    }

    public static class Handler implements IMessageHandler<PacketOpenConfirmation, IMessage>{

        @Override
        public IMessage onMessage(PacketOpenConfirmation message, MessageContext context){
            EntityPlayerMP player = context.getServerHandler().playerEntity;
            player.openGui(Rarmor.instance, message.moduleId, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            return null;
        }
    }
}
