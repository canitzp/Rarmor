/*
 * This file ("WorldData.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.data;

import de.canitzp.rarmor.mod.Rarmor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldData extends WorldSavedData{

    private static final String NAME = Rarmor.MOD_NAME+"Data";

    public Map<UUID, RarmorData> rarmorDataServer = new HashMap<UUID, RarmorData>();
    public Map<UUID, RarmorData> rarmorDataClient = new HashMap<UUID, RarmorData>();

    public WorldData(){
        super(NAME);
    }

    public static Map<UUID, RarmorData> getRarmorData(World world){
        WorldData data = getOrLoadData(world);
        if(data != null){
            if(world.isRemote){
                return data.rarmorDataClient;
            }
            else{
                return data.rarmorDataServer;
            }
        }
        return null;
    }

    public static WorldData getOrLoadData(World world){
        if(world != null){
            MapStorage storage = world.getMapStorage();
            if(storage != null){
                WorldSavedData data = storage.getOrLoadData(WorldData.class, NAME);
                if(data instanceof WorldData){
                    Rarmor.LOGGER.info("Loaded WorldData.");
                    return (WorldData)data;
                }
                else{
                    Rarmor.LOGGER.info("No WorldData found, creating...");

                    WorldData newData = new WorldData();
                    storage.setData(NAME, newData);
                    return newData;
                }
            }
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        this.rarmorDataServer.clear();

        NBTTagList list = compound.getTagList("RarmorData", 10);
        for(int i = 0; i < list.tagCount(); i++){
            NBTTagCompound tag = list.getCompoundTagAt(i);

            UUID id = tag.getUniqueId("RarmorItemId");
            RarmorData data = new RarmorData();
            data.readFromNBT(tag);

            this.rarmorDataServer.put(id, data);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        NBTTagList list = new NBTTagList();
        for(Map.Entry<UUID, RarmorData> entry : this.rarmorDataServer.entrySet()){
            NBTTagCompound tag = new NBTTagCompound();

            tag.setUniqueId("RarmorItemId", entry.getKey());
            entry.getValue().writeToNBT(tag);

            list.appendTag(tag);
        }
        compound.setTag("RarmorData", list);

        return compound;
    }
}
