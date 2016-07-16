package de.canitzp.rarmor.network;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author canitzp
 */
public class PacketSendBoolean implements IMessage, IMessageHandler<PacketSendBoolean, IMessage> {

    private int tabID, key, playerID, worldID;
    private boolean bool;

    public PacketSendBoolean(){}

    public PacketSendBoolean(EntityPlayer player, IRarmorTab tab, int key, boolean bool){
        this.tabID = RarmorAPI.registeredTabs.indexOf(tab.getClass());
        this.key = key;
        this.bool = bool;
        this.playerID = player.getEntityId();
        this.worldID = player.worldObj.provider.getDimension();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.worldID = buf.readInt();
        this.playerID = buf.readInt();
        this.tabID = buf.readInt();
        this.key = buf.readInt();
        this.bool = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.worldID);
        buf.writeInt(this.playerID);
        buf.writeInt(this.tabID);
        buf.writeInt(this.key);
        buf.writeBoolean(this.bool);
    }

    @Override
    public IMessage onMessage(PacketSendBoolean message, MessageContext ctx) {
        World world = DimensionManager.getWorld(message.worldID);
        EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerID);
        if(player != null && RarmorUtil.isPlayerWearingArmor(player)){
            ItemStack stack = RarmorUtil.getRarmorChestplate(player);
            if(stack != null){
                if(RarmorAPI.hasRarmorTabs(player.worldObj, stack)){
                    IRarmorTab tab = RarmorAPI.getTabsFromStack(player.worldObj, stack).get(message.tabID);
                    tab.onPacketBool(player, message.key, message.bool);
                }
            }
        }
        return null;
    }
}
