package de.canitzp.rarmor.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author canitzp
 */
public class PacketSyncColor{

    private int slot, hex, playerID;

    public PacketSyncColor(){}
    
    @OnlyIn(Dist.CLIENT)
    public PacketSyncColor(PacketBuffer buf){
        this.slot = buf.readInt();
        this.hex = buf.readInt();
        this.playerID = buf.readInt();
    }

    @OnlyIn(Dist.CLIENT)
    public PacketSyncColor(EquipmentSlotType slot){
        this.slot = slot.ordinal();
        PlayerEntity player = Minecraft.getInstance().player;
        this.hex = player.inventory.armorInventory.get(slot.getIndex()).getTag().getInt("Color");
        this.playerID = player.getEntityId();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void toBuffer(PacketSyncColor packet, PacketBuffer buf){
        buf.writeInt(packet.slot);
        buf.writeInt(packet.hex);
        buf.writeInt(packet.playerID);
    }
    
    public static void handle(PacketSyncColor packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if(player != null && player.getEntityId() == packet.playerID){
                EquipmentSlotType slot = EquipmentSlotType.values()[packet.slot];
                ItemStack stack = player.inventory.armorInventory.get(slot.getIndex());
                CompoundNBT nbt = stack.hasTag() ? stack.getTag() : new CompoundNBT();
                nbt.putInt("Color", packet.hex);
                stack.setTag(nbt);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
