package de.canitzp.api.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.canitzp.api.util.PlayerUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * @author canitzp
 */
public class PacketOpenGui implements IMessage{

    public int playerID, worldID, guiID;
    public static Object instance;

    public PacketOpenGui(){}

    public PacketOpenGui(EntityPlayer player, int guiID, Object instance){
        this.playerID = player.getEntityId();
        this.worldID = player.getEntityWorld().provider.dimensionId;
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

    public static class Handler implements IMessageHandler<PacketOpenGui, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenGui message, MessageContext ctx){
            World world = DimensionManager.getWorld(message.worldID);
            EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerID);
            PlayerUtil.openInventoryFromServer(player, instance, message.guiID);
            return null;
        }
    }
}
