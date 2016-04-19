package de.canitzp.rarmor.network;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.inventory.GuiHandler;
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

    public static Object instance;
    private int playerID, worldID, guiID;

    public PacketOpenGui(){
    }

    public PacketOpenGui(EntityPlayer player, int guiID, Object instance){
        this.playerID = player.getEntityId();
        this.worldID = player.getEntityWorld().provider.getDimension();
        this.guiID = guiID;
        PacketOpenGui.instance = instance;
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
            EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerID);
            player.openGui(Rarmor.instance, GuiHandler.RFARMORGUI, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            return null;
        }
    }
}
