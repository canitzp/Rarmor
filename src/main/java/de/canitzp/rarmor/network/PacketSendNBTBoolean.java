package de.canitzp.rarmor.network;

import de.canitzp.util.util.NBTUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author canitzp
 */
public class PacketSendNBTBoolean implements IMessage{

    public String nbt;
    public boolean b;
    public int playerID, worldID, playerSlotID;

    public PacketSendNBTBoolean(){}

    /**
     *
     * @param player The EntityPlayerSP
     * @param slotID The id of the slot. Have to be a slotid from PlayerInventory (37: Leggins, 38: Body)
     * @param nbt The Name of the NBT
     * @param b The Value of the NBT
     */
    public PacketSendNBTBoolean(EntityPlayer player, int slotID,  String nbt, boolean b) {
        this.nbt = nbt;
        this.b = b;
        this.playerSlotID = slotID;
        this.playerID = player.getEntityId();
        this.worldID = player.getEntityWorld().provider.getDimensionId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.nbt = ByteBufUtils.readUTF8String(buf);
        this.b = buf.readBoolean();
        this.playerID = buf.readInt();
        this.worldID = buf.readInt();
        this.playerSlotID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.nbt);
        buf.writeBoolean(this.b);
        buf.writeInt(this.playerID);
        buf.writeInt(this.worldID);
        buf.writeInt(this.playerSlotID);
    }

    public static class PacketHandler implements IMessageHandler<PacketSendNBTBoolean, IMessage>{
        @Override
        public IMessage onMessage(PacketSendNBTBoolean message, MessageContext ctx) {
            World world = DimensionManager.getWorld(message.worldID);
            EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerID);
            ItemStack stack = player.inventory.getStackInSlot(message.playerSlotID);
            NBTUtil.setBoolean(stack, message.nbt, message.b);
            return null;
        }
    }
}
