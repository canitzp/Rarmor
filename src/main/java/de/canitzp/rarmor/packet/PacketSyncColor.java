package de.canitzp.rarmor.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class PacketSyncColor implements IMessage, IMessageHandler<PacketSyncColor, IMessage>{

    private int slot, hex, playerID;

    public PacketSyncColor(){}

    @SideOnly(Side.CLIENT)
    public PacketSyncColor(EntityEquipmentSlot slot){
        this.slot = slot.ordinal();
        EntityPlayer player = Minecraft.getMinecraft().player;
        this.hex = player.inventory.armorInventory.get(slot.getIndex()).getTagCompound().getInteger("Color");
        this.playerID = player.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf){
        this.slot = buf.readInt();
        this.hex = buf.readInt();
        this.playerID = buf.readInt();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void toBytes(ByteBuf buf){
        buf.writeInt(this.slot);
        buf.writeInt(this.hex);
        buf.writeInt(this.playerID);
    }

    @Override
    public IMessage onMessage(PacketSyncColor message, MessageContext ctx){
        EntityPlayerMP player = ctx.getServerHandler().player;
        if(player.getEntityId() == message.playerID){
            EntityEquipmentSlot slot = EntityEquipmentSlot.values()[message.slot];
            ItemStack stack = player.inventory.armorInventory.get(slot.getIndex());
            NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            nbt.setInteger("Color", message.hex);
            stack.setTagCompound(nbt);
        }
        return null;
    }
}
