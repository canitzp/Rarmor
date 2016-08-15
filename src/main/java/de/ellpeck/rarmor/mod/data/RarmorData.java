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
import de.ellpeck.rarmor.mod.packet.PacketHandler;
import de.ellpeck.rarmor.mod.packet.PacketSyncRarmorData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

    private boolean sentInitialUpdate;

    private boolean isUpdateQueued;
    private boolean queuedUpdateReload;
    private int queuedUpdateConfirmation;

    public RarmorData(UUID stackId){
        this.stackId = stackId;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean sync){
        NBTTagList data = compound.getTagList("ModuleData", 10);
        for(int i = 0; i < data.tagCount(); i++){
            NBTTagCompound tag = data.getCompoundTagAt(i);

            ActiveRarmorModule module = this.findOrCreateModule(tag.getString("ModuleId"));
            module.readFromNBT(tag, sync);
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

    private ActiveRarmorModule findOrCreateModule(String moduleId){
        for(ActiveRarmorModule module : this.loadedModules){
            if(moduleId.equals(module.getIdentifier())){
                return module;
            }
        }

        ActiveRarmorModule module = Helper.initiateModuleById(moduleId, this);
        this.loadedModules.add(module);
        return module;
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
    public void sendQueuedUpdate(EntityPlayer player){
        if(this.isUpdateQueued && player instanceof EntityPlayerMP){
            PacketHandler.handler.sendTo(new PacketSyncRarmorData(this.stackId, this, this.queuedUpdateReload, this.queuedUpdateConfirmation), (EntityPlayerMP)player);

            this.isUpdateQueued = false;
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
            }
        }
    }

    @Override
    public void tick(World world){
        if(!this.sentInitialUpdate){
            this.queueUpdate(false);
            this.sentInitialUpdate = true;
        }

        for(ActiveRarmorModule module : this.loadedModules){
            if(module != null){
                module.tick(world);
            }
        }
    }

    @Override
    public void queueUpdate(){
        this.queueUpdate(false);
    }

    @Override
    public void queueUpdate(boolean reloadTabs){
        this.queueUpdate(reloadTabs, -1);
    }

    @Override
    public void queueUpdate(boolean reloadTabs, int moduleIdForConfirmation){
        this.queueUpdate(reloadTabs, moduleIdForConfirmation, false);
    }

    @Override
    public void queueUpdate(boolean reloadTabs, int moduleIdForConfirmation, boolean override){
        if(override || !this.isUpdateQueued){
            this.queuedUpdateReload = reloadTabs;
            this.queuedUpdateConfirmation = moduleIdForConfirmation;

            this.isUpdateQueued = true;
        }
    }

    @Override
    public void uninstallModule(ActiveRarmorModule module, EntityPlayer player, int slotIndex){
        if(module != null && this.getCurrentModules().contains(module)){
            module.onUninstalled(player);
            this.getCurrentModules().remove(module);
            this.getSlotToModuleMap().remove(slotIndex);
        }
    }
}
