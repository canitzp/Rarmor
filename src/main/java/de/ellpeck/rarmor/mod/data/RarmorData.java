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

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.api.module.IRarmorModuleItem;
import de.ellpeck.rarmor.mod.item.ItemRarmor;
import de.ellpeck.rarmor.mod.misc.Helper;
import de.ellpeck.rarmor.mod.packet.PacketHandler;
import de.ellpeck.rarmor.mod.packet.PacketSyncRarmorData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class RarmorData implements IRarmorData{

    @SideOnly(Side.CLIENT)
    private static Map<UUID, IRarmorData> tempRarmorDataClient;
    private static Map<UUID, IRarmorData> tempRarmorDataServer;

    private final List<ActiveRarmorModule> loadedModules = new ArrayList<ActiveRarmorModule>();
    private final Map<Integer, String> slotToModulePlaceInListMap = new HashMap<Integer, String>();
    private ItemStack stack;

    private int totalTickedTicks;
    private int selectedModule;

    private boolean sentInitialUpdate;

    private boolean isUpdateQueued;
    private boolean queuedUpdateReload;
    private int queuedUpdateConfirmation;

    public RarmorData(ItemStack stack){
        this.stack = stack;
    }

    public static Map<UUID, IRarmorData> getRarmorData(boolean client){
        if(client){
            if(tempRarmorDataClient == null){
                tempRarmorDataClient = new HashMap<UUID, IRarmorData>();
            }
            return tempRarmorDataClient;
        }
        else{
            if(tempRarmorDataServer == null){
                tempRarmorDataServer = new HashMap<UUID, IRarmorData>();
            }
            return tempRarmorDataServer;
        }
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
        this.totalTickedTicks = compound.getInteger("TotalTicks");

        if(sync){
            this.setEnergy(compound.getInteger("EnergyStored"));
        }
    }

    private ActiveRarmorModule findOrCreateModule(String moduleId){
        ActiveRarmorModule module = this.getInstalledModuleWithId(moduleId);
        if(module == null){
            module = Helper.initiateModuleById(moduleId, this);
            this.loadedModules.add(module);
        }
        return module;
    }

    @Override
    public void selectModule(int i){
        this.selectedModule = i;
    }

    @Override
    public ItemStack getBoundStack(){
        return this.stack;
    }

    @Override
    public void setBoundStack(ItemStack stack){
        this.stack = stack;
    }

    @Override
    public void sendQueuedUpdate(EntityPlayer player){
        if(this.isUpdateQueued && player instanceof EntityPlayerMP){
            UUID id = RarmorAPI.methodHandler.checkAndSetRarmorId(this.stack, false);
            PacketHandler.handler.sendTo(new PacketSyncRarmorData(id, this, this.queuedUpdateReload, this.queuedUpdateConfirmation), (EntityPlayerMP)player);

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
        compound.setInteger("TotalTicks", this.totalTickedTicks);

        if(sync){
            compound.setInteger("EnergyStored", this.getEnergyStored());
        }
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
            String moduleId = ((IRarmorModuleItem)stack.getItem()).getModuleIdentifier();
            if(this.getInstalledModuleWithId(moduleId) == null){
                ActiveRarmorModule module = Helper.initiateModuleById(moduleId, this);
                if(module != null && !this.loadedModules.contains(module)){
                    module.onInstalled(player);
                    this.loadedModules.add(module);
                    this.slotToModulePlaceInListMap.put(slotIndex, module.getIdentifier());
                }
            }
        }
    }

    @Override
    public void tick(World world, Entity entity){
        if(!world.isRemote){
            if(!this.sentInitialUpdate){
                this.queueUpdate();
                this.sentInitialUpdate = true;
            }
        }

        for(ActiveRarmorModule module : this.loadedModules){
            if(module != null){
                module.tick(world, entity);
            }
        }

        this.totalTickedTicks++;
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
    public ActiveRarmorModule getInstalledModuleWithId(String moduleId){
        for(ActiveRarmorModule module : this.loadedModules){
            if(moduleId.equals(module.getIdentifier())){
                return module;
            }
        }
        return null;
    }

    @Override
    public int getTotalTickedTicks(){
        return this.totalTickedTicks;
    }

    @Override
    public int getEnergyStored(){
        ItemRarmor item = this.getEnergyContainer();
        if(item != null){
            return item.getEnergyStored(this.stack);
        }
        return 0;
    }

    @Override
    public int getMaxEnergyStored(){
        ItemRarmor item = this.getEnergyContainer();
        if(item != null){
            return item.getMaxEnergyStored(this.stack);
        }
        return 0;
    }

    @Override
    public int receiveEnergy(int energy, boolean simulate){
        ItemRarmor item = this.getEnergyContainer();
        if(item != null){
            return item.receiveEnergy(this.stack, energy, simulate);
        }
        return 0;
    }

    @Override
    public int extractEnergy(int energy, boolean simulate){
        ItemRarmor item = this.getEnergyContainer();
        if(item != null){
            return item.extractEnergy(this.stack, energy, simulate);
        }
        return 0;
    }

    @Override
    public void setEnergy(int energy){
        ItemRarmor item = this.getEnergyContainer();
        if(item != null){
            item.setEnergy(this.stack, energy);
        }
    }

    private ItemRarmor getEnergyContainer(){
        if(this.stack != null){
            Item item = this.stack.getItem();
            if(item instanceof ItemRarmor){
                return (ItemRarmor)item;
            }
        }
        return null;
    }

    @Override
    public void uninstallModule(ActiveRarmorModule module, EntityPlayer player, int slotIndex){
        if(module != null && this.loadedModules.contains(module) && this.getInstalledModuleWithId(module.getIdentifier()) != null){
            module.onUninstalled(player);
            this.loadedModules.remove(module);
            this.slotToModulePlaceInListMap.remove(slotIndex);
        }
    }
}
