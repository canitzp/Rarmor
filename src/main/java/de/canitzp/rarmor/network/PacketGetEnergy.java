package de.canitzp.rarmor.network;

import cofh.api.energy.IEnergyProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class PacketGetEnergy implements IMessage, IMessageHandler<PacketGetEnergy, IMessage> {

    private int x, y, z, energy;

    public PacketGetEnergy(){}

    public PacketGetEnergy(BlockPos pos, int energy){
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.energy = energy;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.energy = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.energy);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(PacketGetEnergy message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player != null){
            TileEntity tile = player.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if(tile != null){
                NBTTagCompound nbt = tile.serializeNBT();
                nbt.setInteger("Energy", message.energy);
                tile.deserializeNBT(nbt);
            }
        }
        return null;
    }
}
