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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WorldData extends WorldSavedData {

    private static final String NAME = Rarmor.MOD_NAME+"Data";

    private final Map<UUID, IRarmorData> rarmorData = new ConcurrentHashMap<UUID, IRarmorData>();
    private CompoundNBT compoundRead;

    public WorldData(){
        super(NAME);
    }

    public static Map<UUID, IRarmorData> getRarmorData(ServerWorld world){
        WorldData data = getOrLoadData(world);
        if(data != null){
            return data.rarmorData;
        }
        return null;
    }

    public static WorldData getOrLoadData(ServerWorld world){
        if(world != null){
            DimensionSavedDataManager storage = world.getSavedData();
            if(storage != null){
                return storage.getOrCreate(WorldData::new, NAME);
            }
        }
        return null;
    }

    @Override
    public void read(CompoundNBT compound){
        this.rarmorData.clear();

        Set<String> keys = compound.keySet();
        for(String key : keys){
            UUID id = null;
            try{
                id = UUID.fromString(key);
            }
            catch(Exception e){
                Rarmor.LOGGER.error("Found a weird data tag in the world's "+NAME+": "+key+". Ignoring...");
            }

            if(id != null){
                CompoundNBT tag = compound.getCompound(key);

                RarmorData data = new RarmorData(null);
                data.readFromNBT(tag, false);

                this.rarmorData.put(id, data);
            }
        }

        this.compoundRead = compound;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound){
        //To re-save old data even if it wasn't marked dirty a second time
        //This is due to the compound being put into another compound by MC
        //causing all of the data that was previously on there to be lost :v
        if(this.compoundRead != null){
            compound = this.compoundRead;
        }

        for(UUID id : this.rarmorData.keySet()){
            IRarmorData data = this.rarmorData.get(id);
            if(data != null && data.getDirty()){
                CompoundNBT tag = new CompoundNBT();
                data.writeToNBT(tag, false);
                compound.put(id.toString(), tag);

                data.setDirty(false);
            }
        }

        return compound;
    }
}
