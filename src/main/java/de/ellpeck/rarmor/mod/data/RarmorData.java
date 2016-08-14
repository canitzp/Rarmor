/*
 * This file ("RarmorData.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.data;

import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.api.module.IRarmorModuleItem;
import de.ellpeck.rarmor.mod.misc.Helper;
import de.ellpeck.rarmor.mod.module.main.ActiveModuleMain;
import de.ellpeck.rarmor.mod.packet.PacketHandler;
import de.ellpeck.rarmor.mod.packet.PacketSyncRarmorData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.*;

public class RarmorData implements IRarmorData{

    private final List<ActiveRarmorModule> loadedModules = new ArrayList<ActiveRarmorModule>();
    private final Map<Integer, String> slotToModulePlaceInListMap = new HashMap<Integer, String>();
    private final UUID stackId;
    public int selectedModule;

    public RarmorData(UUID stackId){
        this.stackId = stackId;
    }

    public static IRarmorData getDataForChestplate(EntityPlayer player){
        ItemStack stack = player.inventory.armorInventory[EntityEquipmentSlot.CHEST.getIndex()];
        if(stack != null){
            return getDataForStack(player.worldObj, stack);
        }
        return null;
    }

    public static boolean checkAndSetRarmorId(ItemStack stack, boolean set){
        if(!stack.hasTagCompound()){
            if(set){
                stack.setTagCompound(new NBTTagCompound());
            }
            else{
                return false;
            }
        }

        NBTTagCompound compound = stack.getTagCompound();
        if(!compound.hasUniqueId("RarmorId")){
            if(set){
                compound.setUniqueId("RarmorId", UUID.randomUUID());
            }
            return false;
        }
        else{
            return true;
        }
    }

    public static IRarmorData getDataForStack(World world, ItemStack stack){
        checkAndSetRarmorId(stack, true);
        UUID stackId = stack.getTagCompound().getUniqueId("RarmorId");

        Map<UUID, IRarmorData> data = WorldData.getRarmorData(world);
        if(data != null){
            IRarmorData theData = data.get(stackId);
            if(theData == null){
                theData = new RarmorData(stackId);

                ActiveRarmorModule module = Helper.initiateModuleById(ActiveModuleMain.IDENTIFIER, theData);
                module.onInstalled(null);
                theData.getCurrentModules().add(module);

                data.put(stackId, theData);
            }

            return theData;
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean sync){
        NBTTagList data = compound.getTagList("ModuleData", 10);
        for(int i = 0; i < data.tagCount(); i++){
            NBTTagCompound tag = data.getCompoundTagAt(i);

            ActiveRarmorModule module = Helper.initiateModuleById(tag.getString("ModuleId"), this);
            module.readFromNBT(tag, sync);

            this.loadedModules.add(module);
        }

        NBTTagList list = compound.getTagList("SlotToRarmorDataPlaceList", 10);
        for(int i = 0; i < list.tagCount(); i++){
            NBTTagCompound tag = list.getCompoundTagAt(i);

            int key = tag.getInteger("Key");
            String value = tag.getString("Value");

            this.slotToModulePlaceInListMap.put(key, value);
        }

        this.selectedModule = compound.getInteger("SelectedModule");
    }

    @Override
    public void selectModule(int i){
        this.selectedModule = i;
    }

    @Override
    public UUID getBoundStackId(){
        return this.stackId;
    }

    @Override
    public void sendUpdate(EntityPlayer player, boolean reloadTabs, int moduleIdForConfirmation){
        if(!player.worldObj.isRemote && player instanceof EntityPlayerMP){
            PacketHandler.handler.sendTo(new PacketSyncRarmorData(this.stackId, this, reloadTabs, moduleIdForConfirmation), (EntityPlayerMP)player);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, boolean sync){
        NBTTagList data = new NBTTagList();
        for(ActiveRarmorModule module : this.loadedModules){
            NBTTagCompound tag = new NBTTagCompound();

            module.writeToNBT(tag, sync);
            tag.setString("ModuleId", module.getIdentifier());

            data.appendTag(tag);
        }
        compound.setTag("ModuleData", data);

        NBTTagList list = new NBTTagList();
        for(Map.Entry<Integer, String> entry : this.slotToModulePlaceInListMap.entrySet()){
            NBTTagCompound tag = new NBTTagCompound();

            tag.setInteger("Key", entry.getKey());
            tag.setString("Value", entry.getValue());

            list.appendTag(tag);
        }
        compound.setTag("SlotToRarmorDataPlaceList", list);

        compound.setInteger("SelectedModule", this.selectedModule);
    }

    @Override
    public List<ActiveRarmorModule> getCurrentModules(){
        return this.loadedModules;
    }

    @Override
    public Map<Integer, String> getSlotToModuleMap(){
        return this.slotToModulePlaceInListMap;
    }

    @Override
    public int getSelectedModule(){
        return this.selectedModule;
    }

    @Override
    public void installModule(ItemStack stack, EntityPlayer player, int slotIndex){
        if(stack.getItem() instanceof IRarmorModuleItem){
            ActiveRarmorModule module = Helper.initiateModuleById(((IRarmorModuleItem)stack.getItem()).getModuleIdentifier(), this);
            if(module != null && !this.getCurrentModules().contains(module)){
                module.onInstalled(player);
                this.getCurrentModules().add(module);
                this.getSlotToModuleMap().put(slotIndex, module.getIdentifier());

                System.out.println(this.getCurrentModules());
                System.out.println(this.getSlotToModuleMap());
            }
        }
    }

    @Override
    public void uninstallModule(ActiveRarmorModule module, EntityPlayer player, int slotIndex){
        if(module != null && this.getCurrentModules().contains(module)){
            module.onUninstalled(player);
            this.getCurrentModules().remove(module);
            this.getSlotToModuleMap().remove(slotIndex);

            System.out.println(this.getCurrentModules());
            System.out.println(this.getSlotToModuleMap());
        }
    }
}
