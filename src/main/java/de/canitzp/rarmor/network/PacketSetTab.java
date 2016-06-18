package de.canitzp.rarmor.network;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.armor.GuiContainerRarmor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author canitzp
 */
public class PacketSetTab implements IMessage{

    private int tabID, playerID, worldID;

    public PacketSetTab(){}

    public PacketSetTab(EntityPlayer player, IRarmorTab tab){
        this.worldID = player.worldObj.provider.getDimension();
        this.playerID = player.getEntityId();
        this.tabID = RarmorAPI.registeredTabs.indexOf(tab.getClass());
    }

    @Override
    public void fromBytes(ByteBuf buf){
        this.worldID = buf.readInt();
        this.playerID = buf.readInt();
        this.tabID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeInt(this.worldID);
        buf.writeInt(this.playerID);
        buf.writeInt(this.tabID);
    }

    public static class Handler implements IMessageHandler<PacketSetTab, IMessage>{
        @Override
        public IMessage onMessage(PacketSetTab message, MessageContext ctx){
            World world = DimensionManager.getWorld(message.worldID);
            if(world != null){
                EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerID);
                if(player != null){
                    Container container = player.openContainer;
                    if(container != null && container instanceof GuiContainerRarmor){
                        ((GuiContainerRarmor) container).setTabPacket(message.tabID);
                    }
                }
            }
            return null;
        }
    }
}
