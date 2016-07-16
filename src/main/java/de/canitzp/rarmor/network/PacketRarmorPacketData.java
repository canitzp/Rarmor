package de.canitzp.rarmor.network;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author canitzp
 */
public class PacketRarmorPacketData implements IMessage, IMessageHandler<PacketRarmorPacketData, IMessage> {

    private NBTTagCompound nbtTagCompound;
    private int tabID;

    public PacketRarmorPacketData(){}

    public PacketRarmorPacketData(NBTTagCompound nbt, int tabID){
        this.nbtTagCompound = nbt;
        this.tabID = tabID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.nbtTagCompound = ByteBufUtils.readTag(buf);
        this.tabID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.nbtTagCompound);
        buf.writeInt(this.tabID);
    }

    @Override
    public IMessage onMessage(PacketRarmorPacketData message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player != null && message.nbtTagCompound != null){
            if(RarmorUtil.isPlayerWearingArmor(player)){
                ItemStack stack = RarmorUtil.getRarmorChestplate(player);
                if(stack != null){
                    if(RarmorAPI.hasRarmorTabs(player.worldObj, stack)){
                        IRarmorTab tab = RarmorAPI.getTabsFromStack(player.worldObj, stack).get(message.tabID);
                        tab.onPacketData(player, stack, message.nbtTagCompound);
                    }
                }
            }
        }
        return null;
    }
}
