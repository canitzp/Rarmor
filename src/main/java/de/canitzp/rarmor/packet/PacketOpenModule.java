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
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenModule implements IMessage{

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

    @Override
    public void fromBytes(ByteBuf buf){
        this.moduleId = buf.readInt();
        this.alsoSetData = buf.readBoolean();
        this.sendRarmorDataToClient = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeInt(this.moduleId);
        buf.writeBoolean(this.alsoSetData);
        buf.writeBoolean(this.sendRarmorDataToClient);
    }

    public static class Handler implements IMessageHandler<PacketOpenModule, IMessage>{

        @Override
        public IMessage onMessage(final PacketOpenModule message, final MessageContext context){
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable(){
                @Override
                public void run(){
                    EntityPlayer player = context.getServerHandler().playerEntity;
                    if(player != null){
                        RarmorAPI.methodHandler.openRarmor(player, message.moduleId, message.alsoSetData, message.sendRarmorDataToClient);
                    }
                }
            });
            return null;
        }
    }
}
