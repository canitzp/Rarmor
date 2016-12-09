/*
 * This file ("WorldData.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.data;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.Rarmor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WorldData extends WorldSavedData{

    private static final String NAME = Rarmor.MOD_NAME+"Data";

    private final Map<UUID, IRarmorData> rarmorData = new ConcurrentHashMap<UUID, IRarmorData>();
    private NBTTagCompound compoundRead;

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
        this.rarmorData.clear();

        Set<String> keys = compound.getKeySet();
        for(String key : keys){
            UUID id = null;
            try{
                id = UUID.fromString(key);
            }
            catch(Exception e){
                Rarmor.LOGGER.error("Found a weird data tag in the world's "+NAME+": "+key+". Ignoring...");
            }

            if(id != null){
                NBTTagCompound tag = compound.getCompoundTag(key);

                RarmorData data = new RarmorData(null);
                data.readFromNBT(tag, false);

                this.rarmorData.put(id, data);
            }
        }

        this.compoundRead = compound;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        //To re-save old data even if it wasn't marked dirty a second time
        //This is due to the compound being put into another compound by MC
        //causing all of the data that was previously on there to be lost :v
        if(this.compoundRead != null){
            compound = this.compoundRead;
        }

        for(UUID id : this.rarmorData.keySet()){
            IRarmorData data = this.rarmorData.get(id);
            if(data != null && data.getDirty()){
                NBTTagCompound tag = new NBTTagCompound();
                data.writeToNBT(tag, false);
                compound.setTag(id.toString(), tag);

                data.setDirty(false);
            }
        }

        return compound;
    }
}
