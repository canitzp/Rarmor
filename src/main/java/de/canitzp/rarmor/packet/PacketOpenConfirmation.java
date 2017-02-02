/*
 * This file ("PacketOpenConfirmation.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.packet;

import de.canitzp.rarmor.Rarmor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
        public IMessage onMessage(final PacketOpenConfirmation message, final MessageContext context){
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable(){
                @Override
                public void run(){
                    EntityPlayerMP player = context.getServerHandler().player;
                    player.openGui(Rarmor.instance, message.moduleId, player.getEntityWorld(), (int)player.posX, (int)player.posY, (int)player.posZ);
                }
            });
            return null;
        }
    }
}
