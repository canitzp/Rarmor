package de.canitzp.rarmor.network;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author canitzp
 */
public class PacketRequestEnergyLevel implements IMessage, IMessageHandler<PacketRequestEnergyLevel, IMessage>{

    private int x, y, z, side;

    public PacketRequestEnergyLevel(){}

    public PacketRequestEnergyLevel(BlockPos pos, EnumFacing side){
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.side = side.ordinal();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.side = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.side);
    }

    @Override
    public IMessage onMessage(PacketRequestEnergyLevel message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        EnumFacing side = EnumFacing.values()[message.side];
        BlockPos pos = new BlockPos(message.x, message.y, message.z);
        if(player != null){
            TileEntity tile = player.worldObj.getTileEntity(pos);
            if(tile != null){
                if(tile instanceof IEnergyProvider){
                    return new PacketGetEnergy(pos, ((IEnergyProvider) tile).getEnergyStored(side));
                } else if(tile instanceof IEnergyReceiver){
                    return new PacketGetEnergy(pos, ((IEnergyReceiver) tile).getEnergyStored(side));
                } else if(tile instanceof IEnergyStorage){
                    return new PacketGetEnergy(pos, ((IEnergyStorage) tile).getEnergyStored());
                }
            }
        }
        return null;
    }
}
