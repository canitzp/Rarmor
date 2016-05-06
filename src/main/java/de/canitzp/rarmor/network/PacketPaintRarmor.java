/*
 * This file 'PacketPaintRarmor.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.network;

import de.canitzp.rarmor.util.NBTUtil;
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
 * Created by canitzp on 06.05.16.
 */
public class PacketPaintRarmor implements IMessage {

    public int armorSlot, colorValue;
    public String colorText;
    public int playerID, worldID;

    public PacketPaintRarmor(){
    }

    public PacketPaintRarmor(EntityPlayer player, int armorSlot, int colorValue, String colorText){
        this.armorSlot = armorSlot;
        this.colorValue = colorValue;
        this.colorText = colorText;
        this.playerID = player.getEntityId();
        this.worldID = player.getEntityWorld().provider.getDimension();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.worldID = buf.readInt();
        this.playerID = buf.readInt();
        this.armorSlot = buf.readInt();
        this.colorValue = buf.readInt();
        this.colorText = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.worldID);
        buf.writeInt(this.playerID);
        buf.writeInt(this.armorSlot);
        buf.writeInt(this.colorValue);
        ByteBufUtils.writeUTF8String(buf, this.colorText);
    }

    public static class Handler implements IMessageHandler<PacketPaintRarmor, IMessage>{
        @Override
        public IMessage onMessage(PacketPaintRarmor message, MessageContext ctx) {
            World world = DimensionManager.getWorld(message.worldID);
            EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerID);
            ItemStack stack = player.inventory.armorInventory[message.armorSlot];
            NBTUtil.setInteger(stack, "color", message.colorValue);
            NBTUtil.setString(stack, "colorName", message.colorText);
            return null;
        }
    }
}
