/*
 * This file ("PacketSyncRarmorData.java") is part of the Rarmor mod for Minecraft.
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
import de.ellpeck.rarmor.mod.inventory.gui.GuiRarmor;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.UUID;

public class PacketSyncRarmorData implements IMessage{

    private UUID stackId;
    private IRarmorData data;
    private boolean shouldReloadTabs;
    private int moduleIdForConfirmation;

    private NBTTagCompound receivedDataCompound;

    public PacketSyncRarmorData(){

    }

    public PacketSyncRarmorData(UUID stackId, IRarmorData data, boolean shouldReloadTabs, int moduleIdForConfirmation){
        this.stackId = stackId;
        this.data = data;
        this.shouldReloadTabs = shouldReloadTabs;
        this.moduleIdForConfirmation = moduleIdForConfirmation;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        this.shouldReloadTabs = buf.readBoolean();
        this.moduleIdForConfirmation = buf.readInt();

        try{
            PacketBuffer buffer = new PacketBuffer(buf);

            this.stackId = buffer.readUuid();
            this.receivedDataCompound = buffer.readNBTTagCompoundFromBuffer();
        }
        catch(IOException e){
            Rarmor.LOGGER.info("Something went wrong trying to receive a "+Rarmor.MOD_NAME+" Update Packet!", e);
        }
    }

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeBoolean(this.shouldReloadTabs);
        buf.writeInt(this.moduleIdForConfirmation);

        PacketBuffer buffer = new PacketBuffer(buf);

        buffer.writeUuid(this.stackId);

        NBTTagCompound compound = new NBTTagCompound();
        this.data.writeToNBT(compound, true);
        buffer.writeNBTTagCompoundToBuffer(compound);
    }

    public static class Handler implements IMessageHandler<PacketSyncRarmorData, IMessage>{

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketSyncRarmorData message, MessageContext context){
            Minecraft mc = Minecraft.getMinecraft();
            IRarmorData data = RarmorAPI.methodHandler.getDataForUuid(mc.theWorld, message.stackId, true);
            if(data != null){
                data.readFromNBT(message.receivedDataCompound, true);

                if(message.shouldReloadTabs){
                    if(mc.currentScreen instanceof GuiRarmor){
                        ((GuiRarmor)mc.currentScreen).updateTabs();
                    }
                }

                if(message.moduleIdForConfirmation >= 0){
                    PacketHandler.handler.sendToServer(new PacketOpenConfirmation(message.moduleIdForConfirmation));
                }
            }
            return null;
        }
    }
}
