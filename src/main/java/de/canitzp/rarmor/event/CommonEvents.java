/*
 * This file ("CommonEvents.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.event;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.data.WorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RarmorAPI.MOD_ID)
public class CommonEvents{
    private static void doData(ServerWorld world){
        WorldData data = WorldData.getOrLoadData(world);
        if(data != null){
            data.markDirty();
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event){
        if(event.getWorld() instanceof ServerWorld){
            doData((ServerWorld) event.getWorld());
        }
    }

    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Save event){
        if(event.getWorld() instanceof ServerWorld){
            doData((ServerWorld) event.getWorld());
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinWorldEvent event){
        if(!event.getWorld().isRemote){
            Entity entity = event.getEntity();
            if(entity instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity)entity;

                for(int i = 0; i < player.inventory.getSizeInventory(); i++){
                    ItemStack stack = player.inventory.getStackInSlot(i);
                    if(!stack.isEmpty()){
                        IRarmorData data = RarmorAPI.methodHandler.getDataForStack(player.getEntityWorld(), stack, false);
                        if(data != null){
                            data.queueUpdate(true);
                        }
                    }
                }
            }
        }
    }
}
