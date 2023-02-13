/*
 * This file ("WorldData.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.data;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.Rarmor;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.WorldCapabilityData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WorldData implements ICapabilitySerializable<CompoundTag>{

    public static final ResourceLocation KEY = new ResourceLocation(RarmorAPI.MOD_ID, "data");

    public static final WorldData INSTANCE = new WorldData();
    public static final LazyOptional<WorldData> HOLDER = LazyOptional.of(() -> INSTANCE);
    private final Map<UUID, IRarmorData> rarmorData = new ConcurrentHashMap<UUID, IRarmorData>();
    private CompoundTag compoundRead;

    private WorldData(){}

    public static Map<UUID, IRarmorData> getRarmorData(){
        return INSTANCE.rarmorData;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();

        // removed since the markDirty functionality is removed too
        //To re-save old data even if it wasn't marked dirty a second time
        //This is due to the compound being put into another compound by MC
        //causing all of the data that was previously on there to be lost :v
        /*if(this.compoundRead != null){
            compound = this.compoundRead;
        }*/

        for(UUID id : this.rarmorData.keySet()){
            IRarmorData data = this.rarmorData.get(id);
            if(data != null){
                CompoundTag tag = new CompoundTag();
                data.writeToNBT(tag, false);
                compound.put(id.toString(), tag);

                data.setDirty(false);
            }
        }

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        this.rarmorData.clear();

        Set<String> keys = compound.getAllKeys();
        for(String key : keys){
            UUID id = null;
            try{
                id = UUID.fromString(key);
            }
            catch(Exception e){
                Rarmor.LOGGER.error("Found a weird data tag in the world's "+ KEY +": "+key+". Ignoring...");
            }

            if(id != null){
                CompoundTag tag = compound.getCompound(key);

                RarmorData data = new RarmorData(ItemStack.EMPTY);
                data.readFromNBT(tag, false);

                this.rarmorData.put(id, data);
            }
        }

        this.compoundRead = compound;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return RarmorWorldCapability.INSTANCE.orEmpty(cap, HOLDER);
    }
}
