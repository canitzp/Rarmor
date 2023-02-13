/*
 * This file ("PacketSyncRarmorData.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.packet;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.data.RarmorDataCapability;
import de.canitzp.rarmor.inventory.gui.GuiRarmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncRarmorData{

    private UUID stackId;
    private IRarmorData data;
    private boolean shouldReloadTabs;
    private int moduleIdForConfirmation;

    private CompoundTag receivedDataCompound;

    public PacketSyncRarmorData(){}
    
    public PacketSyncRarmorData(UUID stackId, IRarmorData data, boolean shouldReloadTabs, int moduleIdForConfirmation){
        this.stackId = stackId;
        this.data = data;
        this.shouldReloadTabs = shouldReloadTabs;
        this.moduleIdForConfirmation = moduleIdForConfirmation;
    }

    public PacketSyncRarmorData(FriendlyByteBuf buf){
        this.shouldReloadTabs = buf.readBoolean();
        this.moduleIdForConfirmation = buf.readInt();
        this.stackId = buf.readUUID();
        this.receivedDataCompound = buf.readAnySizeNbt();
    }
    
    public static void toBuffer(PacketSyncRarmorData packet, FriendlyByteBuf buf){
        buf.writeBoolean(packet.shouldReloadTabs);
        buf.writeInt(packet.moduleIdForConfirmation);
        buf.writeUUID(packet.stackId);

        CompoundTag compound = new CompoundTag();
        packet.data.writeToNBT(compound, true);
        buf.writeNbt(compound);
    }
    
    public static void handle(PacketSyncRarmorData packet, Supplier<NetworkEvent.Context> ctx){
        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT){
            ctx.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                LocalPlayer player = mc.player;
                if(player != null){
                    for(int i = 0; i < player.getInventory().getContainerSize(); i++){
                        ItemStack stack = player.getInventory().getItem(i);
                        if(!stack.isEmpty()){
                            if(packet.stackId.equals(RarmorAPI.methodHandler.checkAndSetRarmorId(stack, false))){
                                IRarmorData data = RarmorAPI.methodHandler.getDataForStack(mc.level, stack, true);
                                if(data != null){
                                    System.out.println("handle: " + packet.receivedDataCompound);
                                    data.readFromNBT(packet.receivedDataCompound, true);
                    
                                    if(packet.shouldReloadTabs){
                                        if(mc.screen instanceof GuiRarmor){
                                            ((GuiRarmor)mc.screen).updateTabs();
                                        }
                                    }
                    
                                    if(packet.moduleIdForConfirmation >= 0){
                                        PacketHandler.channel.sendToServer(new PacketOpenConfirmation(packet.moduleIdForConfirmation));
                                    }
                                }
                            }
                        }
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
