/*
 * This file ("WorldData.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.data;

import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.mod.Rarmor;
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

    private final Map<UUID, IRarmorData> rarmorData = new HashMap<UUID, IRarmorData>();
    private boolean isLoading;

    public WorldData(String name){
        super(name);
    }

    public static Map<UUID, IRarmorData> getRarmorData(World world){
        WorldData data = getOrLoadData(world);
        if(data != null){
            return data.rarmorData;
        }
        return null;
    }

    public static WorldData getOrLoadData(World world){
        if(world != null){
            MapStorage storage = world.getMapStorage();
            if(storage != null){
                WorldSavedData data = storage.getOrLoadData(WorldData.class, NAME);
                if(data instanceof WorldData){
                    return (WorldData)data;
                }
                else{
                    WorldData newData = new WorldData(NAME);
                    storage.setData(NAME, newData);
                    return newData;
                }
            }
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        this.isLoading = true;

        this.rarmorData.clear();

        NBTTagList list = compound.getTagList("RarmorData", 10);
        for(int i = 0; i < list.tagCount(); i++){
            NBTTagCompound tag = list.getCompoundTagAt(i);

            UUID id = tag.getUniqueId("RarmorItemId");
            IRarmorData data = new RarmorData(id);
            data.readFromNBT(tag, false);

            this.rarmorData.put(id, data);
        }

        this.isLoading = false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        if(!this.isLoading){
            NBTTagList list = new NBTTagList();
            for(Map.Entry<UUID, IRarmorData> entry : this.rarmorData.entrySet()){
                NBTTagCompound tag = new NBTTagCompound();

                tag.setUniqueId("RarmorItemId", entry.getKey());
                entry.getValue().writeToNBT(tag, false);

                list.appendTag(tag);
            }
            compound.setTag("RarmorData", list);
        }

        return compound;
    }
}
