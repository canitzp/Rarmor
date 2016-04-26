package de.canitzp.rarmor.network;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.inventory.GuiHandler;
import de.canitzp.rarmor.util.MinecraftUtil;
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

    private int playerID, worldID, guiID;

    public PacketOpenGui(){
    }

    public PacketOpenGui(EntityPlayer player, int guiID){
        this.playerID = player.getEntityId();
        this.worldID = player.getEntityWorld().provider.getDimension();
        this.guiID = guiID;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        playerID = buf.readInt();
        worldID = buf.readInt();
        guiID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeInt(playerID);
        buf.writeInt(worldID);
        buf.writeInt(guiID);
    }

    public static class Handler implements IMessageHandler<PacketOpenGui, IMessage>{

        @Override
        public IMessage onMessage(PacketOpenGui message, MessageContext ctx){
            World world = DimensionManager.getWorld(message.worldID);
            if(!world.isRemote){
                EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerID);
                if(player != null){
                    player.openGui(Rarmor.instance, GuiHandler.RFARMORGUI, world, (int) player.posX, (int) player.posY, (int) player.posZ);
                } else {
                    System.out.println(MinecraftUtil.getPlayer().dimension);
                }
            }
            return null;
        }
    }
}
