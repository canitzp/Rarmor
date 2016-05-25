package de.canitzp.rarmor.newnetwork;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldEvents{

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event){
        WorldData.makeDirty();
    }

    @SubscribeEvent
    public void onUnload(WorldEvent.Unload event){
        WorldData.makeDirty();
    }

    @SubscribeEvent
    public void onSave(WorldEvent.Save event){
        WorldData.makeDirty();
    }
}