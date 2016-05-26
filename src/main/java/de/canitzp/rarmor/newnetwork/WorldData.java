package de.canitzp.rarmor.newnetwork;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldData extends WorldSavedData{

    public static final String DATA_TAG = "rarmordata";
    public static WorldData instance;

    @SideOnly(Side.CLIENT)
    private static Map<UUID, RarmorData> rarmorClientData;
    private static Map<UUID, RarmorData> rarmorServerData = new HashMap<UUID, RarmorData>();

    public static Map<UUID, RarmorData> getRarmorData(boolean client){
        if(client){
            if(rarmorClientData == null){
                rarmorClientData = new HashMap<UUID, RarmorData>();
            }
            return rarmorClientData;
        }
        else{
            return rarmorServerData;
        }
    }

    public WorldData(String tag){
        super(tag);
    }

    public static void makeDirty(){
        if(instance != null){
            instance.markDirty();
        }
    }

    public static void init(MinecraftServer server){
        if(server != null){
            World world = server.getEntityWorld();
            if(!world.isRemote){
                //ModUtil.LOGGER.info("Loading WorldData!");

                WorldData savedData = (WorldData)world.loadItemData(WorldData.class, DATA_TAG);
                //Generate new SavedData
                if(savedData == null){
                    //ModUtil.LOGGER.info("No WorldData found, creating...");

                    savedData = new WorldData(DATA_TAG);
                    world.setItemData(DATA_TAG, savedData);
                }
                else{
                    //ModUtil.LOGGER.info("WorldData sucessfully received!");
                }

                //Set the current SavedData to the retreived one
                WorldData.instance = savedData;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        NBTTagList list = compound.getTagList("RarmorData", 10);
        for(int i = 0; i < list.tagCount(); i++){
            NBTTagCompound dataComp = list.getCompoundTagAt(i);
            rarmorServerData.put(dataComp.getUniqueId("ID"), RarmorData.readFromNBT(dataComp, false));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        NBTTagList list = new NBTTagList();
        for(Map.Entry<UUID, RarmorData> data : rarmorServerData.entrySet()){
            NBTTagCompound dataComp = new NBTTagCompound();
            data.getValue().writeToNBT(dataComp, false);
            dataComp.setUniqueId("ID", data.getKey());
            list.appendTag(dataComp);
        }
        compound.setTag("RarmorData", list);
        return compound;
    }
}