package de.canitzp.rarmor.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
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
public class PacketSetNBTInPlayerInventory implements IMessage, IMessageHandler<PacketSetNBTInPlayerInventory, IMessage> {

    private NBTTagCompound nbt;
    private int slotNumber;

    public PacketSetNBTInPlayerInventory(){}

    public PacketSetNBTInPlayerInventory(NBTTagCompound nbt, int slotNumber){
        this.nbt = nbt;
        this.slotNumber = slotNumber;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.nbt = ByteBufUtils.readTag(buf);
        this.slotNumber = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.nbt);
        buf.writeInt(this.slotNumber);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(PacketSetNBTInPlayerInventory message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player != null){
            InventoryPlayer inv = player.inventory;
            ItemStack stack = inv.getStackInSlot(message.slotNumber);
            if(stack != null){
                NBTTagCompound nbt = stack.getTagCompound();
                if(nbt != null){
                    System.out.println("merge" + message.nbt);
                    nbt.merge(message.nbt);
                }
            }
        }
        return null;
    }
}
