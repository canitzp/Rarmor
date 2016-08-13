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

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.mod.misc.Helper;
import de.canitzp.rarmor.mod.module.main.ActiveModuleMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.*;

public class RarmorData implements IRarmorData{

    private final List<ActiveRarmorModule> loadedModules = new ArrayList<ActiveRarmorModule>();
    private final Map<Integer, Integer> slotToModulePlaceInListMap = new HashMap<Integer, Integer>();
    public int selectedModule;

    @Override
    public void readFromNBT(NBTTagCompound compound){
        NBTTagList data = compound.getTagList("ModuleData", 10);
        for(int i = 0; i < data.tagCount(); i++){
            NBTTagCompound tag = data.getCompoundTagAt(i);

            ActiveRarmorModule module = Helper.initiateModuleById(tag.getString("ModuleId"));
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

        this.selectedModule = compound.getInteger("SelectedModule");
    }

    @Override
    public void selectModule(int i){
        this.selectedModule = i;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound){
        NBTTagList data = new NBTTagList();
        for(ActiveRarmorModule module : this.loadedModules){
            NBTTagCompound tag = new NBTTagCompound();

            module.writeToNBT(tag);
            tag.setString("ModuleId", module.getIdentifier());

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

        compound.setInteger("SelectedModule", this.selectedModule);
    }

    public static IRarmorData getDataForChestplate(EntityPlayer player){
        ItemStack stack = player.inventory.armorInventory[EntityEquipmentSlot.CHEST.getIndex()];
        if(stack != null){
            return getDataForStack(player.worldObj, stack);
        }
        return null;
    }

    public static IRarmorData getDataForStack(World world, ItemStack stack){
        Map<UUID, IRarmorData> data = WorldData.getRarmorData(world);
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

            IRarmorData theData = data.get(stackId);
            if(theData == null){
                theData = new RarmorData();

                ActiveRarmorModule module = Helper.initiateModuleById(ActiveModuleMain.IDENTIFIER);
                module.onInstalled(null);
                theData.getCurrentModules().add(module);

                data.put(stackId, theData);
            }

            return theData;
        }
        else{
            return null;
        }
    }

    @Override
    public List<ActiveRarmorModule> getCurrentModules(){
        return this.loadedModules;
    }

    @Override
    public Map<Integer, Integer> getSlotToModuleMap(){
        return this.slotToModulePlaceInListMap;
    }

    @Override
    public int getSelectedModule(){
        return this.selectedModule;
    }
}
