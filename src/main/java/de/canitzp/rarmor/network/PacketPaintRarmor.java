package de.canitzp.rarmor.network;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.armor.RarmorColoringTab;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author canitzp
 */
public class PacketPaintRarmor implements IMessage {

	public int armorSlot;
	public RarmorColoringTab.Color color;

	public PacketPaintRarmor() {

	}

	public PacketPaintRarmor(EntityEquipmentSlot armor, RarmorColoringTab.Color color) {
		this.armorSlot = armor.getIndex();
		this.color = color;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.armorSlot = buf.readInt();
		int colorHex = buf.readInt();
		this.color = new RarmorColoringTab.Color(colorHex, RarmorAPI.registerColor.get(colorHex));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.armorSlot);
		buf.writeInt(this.color.hexValue);
	}

	public static class Handler implements IMessageHandler<PacketPaintRarmor, IMessage> {
		@Override
		public IMessage onMessage(PacketPaintRarmor message, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().playerEntity;
			if (player != null) {
				ItemStack stack = player.inventory.armorInventory[message.armorSlot];
				NBTUtil.setColor(stack, message.color);
			}
			return null;
		}
	}
}
