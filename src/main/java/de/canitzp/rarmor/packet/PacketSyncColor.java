package de.canitzp.rarmor.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author canitzp
 */
public class PacketSyncColor{

    private int slot, hex, playerID;

    public PacketSyncColor(){}
    
    @OnlyIn(Dist.CLIENT)
    public PacketSyncColor(FriendlyByteBuf buf){
        this.slot = buf.readInt();
        this.hex = buf.readInt();
        this.playerID = buf.readInt();
    }

    @OnlyIn(Dist.CLIENT)
    public PacketSyncColor(EquipmentSlot slot){
        this.slot = slot.ordinal();
        Player player = Minecraft.getInstance().player;
        this.hex = ((DyeableArmorItem) player.getInventory().armor.get(slot.getIndex()).getItem()).getColor(player.getInventory().armor.get(slot.getIndex()));
        this.playerID = player.getId();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void toBuffer(PacketSyncColor packet, FriendlyByteBuf buf){
        buf.writeInt(packet.slot);
        buf.writeInt(packet.hex);
        buf.writeInt(packet.playerID);
    }
    
    public static void handle(PacketSyncColor packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if(player != null && player.getId() == packet.playerID){
                EquipmentSlot slot = EquipmentSlot.values()[packet.slot];
                ItemStack stack = player.getInventory().armor.get(slot.getIndex());
                ((DyeableArmorItem) stack.getItem()).setColor(stack, packet.hex);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
