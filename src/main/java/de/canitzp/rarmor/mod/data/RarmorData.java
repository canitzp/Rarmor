/*
 * This file ("RarmorData.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.data;

import de.canitzp.rarmor.api.module.IActiveRarmorModule;
import de.canitzp.rarmor.mod.misc.Helper;
import de.canitzp.rarmor.mod.module.main.ActiveModuleMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.*;

public class RarmorData{

    public List<IActiveRarmorModule> loadedModules = new ArrayList<IActiveRarmorModule>();
    public Map<Integer, Integer> slotToModulePlaceInListMap = new HashMap<Integer, Integer>();
    public int guiToOpen;

    public void readFromNBT(NBTTagCompound compound){
        NBTTagList data = compound.getTagList("ModuleData", 10);
        for(int i = 0; i < data.tagCount(); i++){
            NBTTagCompound tag = data.getCompoundTagAt(i);

            IActiveRarmorModule module = Helper.initiateModuleById(tag.getString("ModuleId"));
            module.readFromNBT(tag);

            this.loadedModules.add(module);
        }

        NBTTagList list = compound.getTagList("SlotToRarmorDataPlaceList", 10);
        for(int i = 0; i < list.tagCount(); i++){
            NBTTagCompound tag = list.getCompoundTagAt(i);

            int key = tag.getInteger("Key");
            int value = tag.getInteger("Value");

            this.slotToModulePlaceInListMap.put(key, value);
        }
    }

    public void writeToNBT(NBTTagCompound compound){
        NBTTagList data = new NBTTagList();
        for(IActiveRarmorModule module : this.loadedModules){
            NBTTagCompound tag = new NBTTagCompound();

            module.writeToNBT(tag);
            tag.setString("ModuleId", Helper.getIdFromModule(module));

            data.appendTag(tag);
        }
        compound.setTag("ModuleData", data);

        NBTTagList list = new NBTTagList();
        for(Map.Entry<Integer, Integer> entry : this.slotToModulePlaceInListMap.entrySet()){
            NBTTagCompound tag = new NBTTagCompound();

            tag.setInteger("Key", entry.getKey());
            tag.setInteger("Value", entry.getValue());

            list.appendTag(tag);
        }
        compound.setTag("SlotToRarmorDataPlaceList", list);
    }

    public static RarmorData getDataForChestplate(EntityPlayer player){
        ItemStack stack = player.inventory.armorInventory[EntityEquipmentSlot.CHEST.getIndex()];
        if(stack != null){
            return getDataForStack(player.worldObj, stack);
        }
        return null;
    }

    public static RarmorData getDataForStack(World world, ItemStack stack){
        Map<UUID, RarmorData> data = WorldData.getRarmorData(world);
        if(data != null){
            UUID stackId = null;

            boolean hasCompound = stack.hasTagCompound();
            if(hasCompound){
                NBTTagCompound compound = stack.getTagCompound();
                if(compound.hasKey("RarmorIdMost")){
                    stackId = compound.getUniqueId("RarmorId");
                }
            }

            if(stackId == null){
                if(!hasCompound){
                    stack.setTagCompound(new NBTTagCompound());
                }

                stackId = UUID.randomUUID();
                stack.getTagCompound().setUniqueId("RarmorId", stackId);
            }

            RarmorData theData = data.get(stackId);
            if(theData == null){
                theData = new RarmorData();

                IActiveRarmorModule module = Helper.initiateModuleById(ActiveModuleMain.IDENTIFIER);
                module.onInstalled(null);
                theData.loadedModules.add(module);

                data.put(stackId, theData);
            }

            return theData;
        }
        else{
            return null;
        }
    }
}
