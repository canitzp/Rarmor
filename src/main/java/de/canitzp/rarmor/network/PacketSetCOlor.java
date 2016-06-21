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
public class PacketSetCOlor implements IMessage{

    public int worldID, playerID, armorSlot;
    public RarmorColoringTab.Color color;

    public PacketSetCOlor(){

    }

    public PacketSetCOlor(EntityPlayer player, EntityEquipmentSlot armor, RarmorColoringTab.Color color){
        this.worldID = player.getEntityWorld().provider.getDimension();
        this.playerID = player.getEntityId();
        this.armorSlot = armor.getIndex();
        this.color = color;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        this.playerID = buf.readInt();
        this.worldID = buf.readInt();
        this.armorSlot = buf.readInt();
        int colorHex = buf.readInt();
        this.color = new RarmorColoringTab.Color(colorHex, RarmorAPI.registerColor.get(colorHex));
    }

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeInt(this.playerID);
        buf.writeInt(this.worldID);
        buf.writeInt(this.armorSlot);
        buf.writeInt(this.color.hexValue);
    }

    public static class Handler implements IMessageHandler<PacketSetCOlor, IMessage>{
        @Override
        public IMessage onMessage(PacketSetCOlor message, MessageContext ctx){
            World world = DimensionManager.getWorld(message.worldID);
            if(world != null){
                EntityPlayer player = (EntityPlayer) world.getEntityByID(message.playerID);
                if(player != null){
                    ItemStack stack = player.inventory.armorInventory[message.armorSlot];
                    NBTUtil.setColor(stack, message.color);
                }
            }
            return null;
        }
    }
}
