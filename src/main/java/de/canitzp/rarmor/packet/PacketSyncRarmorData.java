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
import de.canitzp.rarmor.inventory.gui.GuiRarmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncRarmorData{

    private UUID stackId;
    private IRarmorData data;
    private boolean shouldReloadTabs;
    private int moduleIdForConfirmation;

    private CompoundNBT receivedDataCompound;

    public PacketSyncRarmorData(){}
    
    public PacketSyncRarmorData(UUID stackId, IRarmorData data, boolean shouldReloadTabs, int moduleIdForConfirmation){
        this.stackId = stackId;
        this.data = data;
        this.shouldReloadTabs = shouldReloadTabs;
        this.moduleIdForConfirmation = moduleIdForConfirmation;
    }

    public PacketSyncRarmorData(PacketBuffer buf){
        this.shouldReloadTabs = buf.readBoolean();
        this.moduleIdForConfirmation = buf.readInt();
        this.stackId = buf.readUniqueId();
        this.receivedDataCompound = buf.readCompoundTag();
    }
    
    public static void toBuffer(PacketSyncRarmorData packet, PacketBuffer buf){
        buf.writeBoolean(packet.shouldReloadTabs);
        buf.writeInt(packet.moduleIdForConfirmation);
        buf.writeUniqueId(packet.stackId);

        CompoundNBT compound = new CompoundNBT();
        packet.data.writeToNBT(compound, true);
        buf.writeCompoundTag(compound);
    }
    
    public static void handle(PacketSyncRarmorData packet, Supplier<NetworkEvent.Context> ctx){
        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT){
            ctx.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                ClientPlayerEntity player = mc.player;
                if(player != null){
                    for(int i = 0; i < player.inventory.getSizeInventory(); i++){
                        ItemStack stack = player.inventory.getStackInSlot(i);
                        if(!stack.isEmpty()){
                            if(packet.stackId.equals(RarmorAPI.methodHandler.checkAndSetRarmorId(stack, false))){
                                IRarmorData data = RarmorAPI.methodHandler.getDataForStack(mc.world, stack, true);
                                if(data != null){
                                    data.readFromNBT(packet.receivedDataCompound, true);
                    
                                    if(packet.shouldReloadTabs){
                                        if(mc.currentScreen instanceof GuiRarmor){
                                            ((GuiRarmor)mc.currentScreen).updateTabs();
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
