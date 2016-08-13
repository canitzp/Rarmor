/*
 * This file ("CommonEvents.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.event;

import de.canitzp.rarmor.mod.data.WorldData;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonEvents{

    public CommonEvents(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event){
        loadAndMarkWorldData(event.getWorld());
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event){
        loadAndMarkWorldData(event.getWorld());
    }

    private static void loadAndMarkWorldData(World world){
        if(world != null && !world.isRemote){
            WorldData data = WorldData.getOrLoadData(world);
            if(data != null){
                data.markDirty();
            }
        }
    }
}
