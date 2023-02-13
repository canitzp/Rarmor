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
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = RarmorAPI.MOD_ID)
public class CommonEvents{

/*    @SubscribeEvent
    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        if(event.getObject().dimension() == Level.OVERWORLD){ // only save once; data is the same for every dimension
            event.addCapability(WorldData.KEY, WorldData.INSTANCE);
        }
    }*/

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinWorldEvent event){
        if(!event.getWorld().isClientSide()){
            Entity entity = event.getEntity();
            if(entity instanceof Player){
                Player player = (Player)entity;

                for(int i = 0; i < player.getInventory().getContainerSize(); i++){
                    ItemStack stack = player.getInventory().getItem(i);
                    if(!stack.isEmpty()){
                        IRarmorData data = RarmorAPI.methodHandler.getDataForStack(player.getLevel(), stack, false);
                        if(data != null){
                            data.queueUpdate(true);
                        }
                    }
                }
            }
        }
    }
}
