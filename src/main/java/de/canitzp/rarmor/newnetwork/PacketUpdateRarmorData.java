package de.canitzp.rarmor.newnetwork;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class PacketUpdateRarmorData implements IMessage{

    private UUID id;
    private RarmorData data;

    @SuppressWarnings("unused")
    public PacketUpdateRarmorData(){

    }

    public PacketUpdateRarmorData(UUID id, RarmorData data){
        this.id = id;
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        PacketBuffer buffer = new PacketBuffer(buf);
        try{
            this.data = new RarmorData();
            data.readFromNBT(buffer.readNBTTagCompoundFromBuffer(), true);
            this.id = buffer.readUuid();
        }
        catch(Exception e){
            //ModUtil.LOGGER.error("Something went wrong trying to receive a TileEntity packet!", e);
        }
    }

    @Override
    public void toBytes(ByteBuf buf){
        PacketBuffer buffer = new PacketBuffer(buf);

        NBTTagCompound compound = new NBTTagCompound();
        this.data.writeToNBT(compound, true);
        buffer.writeNBTTagCompoundToBuffer(compound);
        buffer.writeUuid(this.id);
    }

    public static class Handler implements IMessageHandler<PacketUpdateRarmorData, IMessage>{

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketUpdateRarmorData message, MessageContext ctx){
            if(message.id != null && message.data != null){
                WorldData.getRarmorData(true).put(message.id, message.data);
            }
            return null;
        }
    }
}