/*
 * This file ("PacketOpenModule.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.packet;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.mod.Rarmor;
import de.ellpeck.rarmor.mod.misc.Config;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
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
        public IMessage onMessage(PacketOpenModule message, MessageContext context){
            EntityPlayerMP player = context.getServerHandler().playerEntity;
            IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player);
            if(data != null){
                if(message.alsoSetData){
                    if(data != null){
                        data.selectModule(message.moduleId);
                    }
                }

                boolean shouldOpenGui;
                if(message.sendRarmorDataToClient){
                    boolean doPacket = Config.doOpeningConfirmationPacket;

                    shouldOpenGui = !doPacket;
                    data.queueUpdate(true, doPacket ? message.moduleId : -1, true);
                }
                else{
                    shouldOpenGui = true;
                }

                if(shouldOpenGui){
                    player.openGui(Rarmor.instance, message.moduleId, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
                }
            }
            return null;
        }
    }
}
