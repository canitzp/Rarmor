package de.canitzp.rarmor.network;

import de.canitzp.rarmor.GuiIWTSettings;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class PacketSetPlayerNBT implements IMessage, IMessageHandler<PacketSetPlayerNBT, IMessage> {

    private NBTTagCompound nbt;

    public PacketSetPlayerNBT(){}

    public PacketSetPlayerNBT(NBTTagCompound nbt){
        this.nbt = nbt;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.nbt);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(PacketSetPlayerNBT message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(message.nbt != null){
            player.getEntityData().setTag(GuiIWTSettings.DATA_NAME, message.nbt);
        }
        return null;
    }
}
