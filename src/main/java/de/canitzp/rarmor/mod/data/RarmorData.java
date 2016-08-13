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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RarmorData{

    public List<IActiveRarmorModule> loadedModules = new ArrayList<IActiveRarmorModule>();
    public int guiToOpen;

    public void readFromNBT(NBTTagCompound compound){
        NBTTagList list = compound.getTagList("ModuleData", 10);
        for(int i = 0; i < list.tagCount(); i++){
            NBTTagCompound tag = list.getCompoundTagAt(i);

            IActiveRarmorModule module = Helper.initiateModuleById(tag.getString("ModuleId"));
            module.readFromNBT(tag);

            this.loadedModules.add(module);
        }
    }

    public void writeToNBT(NBTTagCompound compound){
        NBTTagList list = new NBTTagList();

        for(IActiveRarmorModule module : this.loadedModules){
            NBTTagCompound tag = new NBTTagCompound();

            module.writeToNBT(tag);
            tag.setString("ModuleId", Helper.getIdFromModule(module));

            list.appendTag(tag);
        }

        compound.setTag("ModuleData", list);
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
                theData.loadedModules.add(Helper.initiateModuleById(ActiveModuleMain.IDENTIFIER));
                data.put(stackId, theData);
            }

            return theData;
        }
        else{
            return null;
        }
    }
}
