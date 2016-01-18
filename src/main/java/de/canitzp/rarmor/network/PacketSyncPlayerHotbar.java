package de.canitzp.rarmor.network;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashMap;
import java.util.List;

/**
 * @author canitzp
 */
public class PacketSyncPlayerHotbar implements IMessage {

    private int playerId, worldId;
    private InventoryPlayer inventoryPlayer;
    private ItemStack[] stackList = new ItemStack[9];

    public PacketSyncPlayerHotbar(){}

    public PacketSyncPlayerHotbar(EntityPlayer player){
        this.inventoryPlayer = player.inventory;
        this.playerId = player.getEntityId();
        this.worldId = player.getEntityWorld().provider.getDimensionId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.worldId = buf.readInt();
        this.playerId = buf.readInt();
        for(int i = 0; i < 9; i++){
            this.stackList[i] = ByteBufUtils.readItemStack(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.worldId);
        buf.writeInt(this.playerId);
        for(int i = 0; i < 9; i++){
            ByteBufUtils.writeItemStack(buf, inventoryPlayer.getStackInSlot(i));
        }
    }

    public static class Handler implements IMessageHandler<PacketSyncPlayerHotbar, IMessage>{
        @Override
        public IMessage onMessage(PacketSyncPlayerHotbar message, MessageContext ctx) {
            World world = DimensionManager.getWorld(message.worldId);
            EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerId);
            for (int i = 0; i < 9; i++){
                player.inventory.setInventorySlotContents(i, message.stackList[i]);
            }
            return null;
        }
    }
}
