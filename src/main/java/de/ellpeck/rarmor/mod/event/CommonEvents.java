/*
 * This file ("CommonEvents.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.event;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.mod.data.WorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonEvents{

    public CommonEvents(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event){
        doData(event.getWorld());
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event){
        doData(event.getWorld());
    }

    private static void doData(World world){
        if(!world.isRemote){
            WorldData data = WorldData.getOrLoadData(world);
            if(data != null){
                data.markDirty();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event){
        if(!event.getWorld().isRemote){
            Entity entity = event.getEntity();
            if(entity instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer)entity;

                for(int i = 0; i < player.inventory.getSizeInventory(); i++){
                    ItemStack stack = player.inventory.getStackInSlot(i);
                    if(stack != null){
                        IRarmorData data = RarmorAPI.methodHandler.getDataForStack(player.worldObj, stack, false);
                        if(data != null){
                            data.queueUpdate(true);
                        }
                    }
                }
            }
        }
    }
}
