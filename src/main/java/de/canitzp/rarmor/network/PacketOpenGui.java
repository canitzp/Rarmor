package de.canitzp.rarmor.network;

import de.canitzp.rarmor.Rarmor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author canitzp
 */
public class PacketOpenGui implements IMessage{

    private int worldID, playerID, guiID;

    public PacketOpenGui(){}

    public PacketOpenGui(EntityPlayer player, int guiID){
        this.worldID = player.worldObj.provider.getDimension();
        this.playerID = player.getEntityId();
        this.guiID = guiID;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        this.worldID = buf.readInt();
        this.playerID = buf.readInt();
        this.guiID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeInt(this.worldID);
        buf.writeInt(this.playerID);
        buf.writeInt(this.guiID);
    }

    public static class Handler implements IMessageHandler<PacketOpenGui, IMessage>{
        @Override
        public IMessage onMessage(PacketOpenGui message, MessageContext ctx){
            World world = DimensionManager.getWorld(message.worldID);
            if(world != null){
                EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerID);
                if(player != null){
                    player.openGui(Rarmor.instance, message.guiID, world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
                }
            }
            return null;
        }
    }
}
