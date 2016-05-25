package de.canitzp.rarmor.newnetwork;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.Map;
import java.util.UUID;

public class RarmorData{

    public int saveAndSyncInt;
    public int onlySaveInt;

    public void writeToNBT(NBTTagCompound compound, boolean forSync){
        if(!forSync){
            compound.setInteger("Save", this.onlySaveInt);
        }
        compound.setInteger("Sync", this.saveAndSyncInt);
    }

    public void readFromNBT(NBTTagCompound compound, boolean forSync){
        if(!forSync){
            this.onlySaveInt = compound.getInteger("Save");
        }
        this.saveAndSyncInt = compound.getInteger("Sync");
    }

    public IMessage getSyncMessage(){
        for(Map.Entry<UUID, RarmorData> data : WorldData.getRarmorData(false).entrySet()){
            if(data.getValue() == this){
                return new PacketUpdateRarmorData(data.getKey(), this);
            }
        }
        return null;
    }

    public static RarmorData getDataForRarmor(ItemStack stack, boolean client){
        if(!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound compound = stack.getTagCompound();

        if(compound.hasKey("RarmorIDLeast")){
            RarmorData data = WorldData.getRarmorData(client).get(compound.getUniqueId("RarmorID"));
            if(data != null){
                return data;
            }
        }

        RarmorData newData = new RarmorData();
        UUID newID = UUID.randomUUID();
        compound.setUniqueId("RarmorID", newID);
        WorldData.getRarmorData(client).put(newID, newData);
        return newData;
    }

    @Override
    public String toString(){
        return this.saveAndSyncInt + " "+this.onlySaveInt;
    }
}
