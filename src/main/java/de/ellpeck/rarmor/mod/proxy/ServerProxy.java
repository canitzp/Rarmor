/*
 * This file ("ServerProxy.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.proxy;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy implements IProxy{

    @Override
    public void preInit(FMLPreInitializationEvent event){

    }

    @Override
    public void init(FMLInitializationEvent event){

    }

    @Override
    public void addWeirdRunnablePacketThing(Runnable runnable){
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(runnable);
    }
}
