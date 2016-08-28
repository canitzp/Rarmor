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

import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.mod.data.RarmorData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonEvents{

    public CommonEvents(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event){
        if(!event.getWorld().isRemote){
            for(IRarmorData data : RarmorData.getRarmorData(false).values()){
                ItemStack stack = data.getBoundStack();

                NBTTagCompound compound = new NBTTagCompound();
                data.writeToNBT(compound, false);

                if(!stack.hasTagCompound()){
                    stack.setTagCompound(new NBTTagCompound());
                }
                stack.getTagCompound().setTag("RarmorData", compound);

                data.setDeleteStackDataOnFetch(true);
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event){
        RarmorData.getRarmorData(event.getWorld().isRemote).clear();
    }
}
