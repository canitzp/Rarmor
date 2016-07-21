package de.canitzp.rarmor.network;

import de.canitzp.rarmor.GuiIWTSettings;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author canitzp
 */
public class PacketAddToPlayerNBT implements IMessage, IMessageHandler<PacketAddToPlayerNBT, IMessage> {

    private NBTTagCompound nbt;
    private int playerID;

    public PacketAddToPlayerNBT(){}

    public PacketAddToPlayerNBT(EntityPlayer player, NBTTagCompound nbt){
        this.nbt = nbt;
        this.playerID = player.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerID = buf.readInt();
        this.nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.playerID);
        ByteBufUtils.writeTag(buf, this.nbt);
    }

    @Override
    public IMessage onMessage(PacketAddToPlayerNBT message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        if(player != null && player.getEntityId() == message.playerID){
            if(message.nbt != null){
                player.getEntityData().setTag(GuiIWTSettings.DATA_NAME, message.nbt);
            }
        }
        return null;
    }
}
