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
public class PacketOpenGui implements IMessage {

	private int guiID;

	public PacketOpenGui() {
	}

	public PacketOpenGui(int guiID) {
		this.guiID = guiID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.guiID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.guiID);
	}

	public static class Handler implements IMessageHandler<PacketOpenGui, IMessage> {
		@Override
		public IMessage onMessage(PacketOpenGui message, MessageContext ctx) {
			EntityPlayer player=ctx.getServerHandler().playerEntity;
			player.openGui(Rarmor.instance, message.guiID, player.worldObj, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			return null;
		}
	}
}
