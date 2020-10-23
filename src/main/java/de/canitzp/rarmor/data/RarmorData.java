/*
 * This file ("RarmorData.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.data;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.api.module.IRarmorModuleItem;
import de.canitzp.rarmor.inventory.gui.BasicInventory;
import de.canitzp.rarmor.item.ItemRarmorChest;
import de.canitzp.rarmor.packet.PacketHandler;
import de.canitzp.rarmor.packet.PacketSyncRarmorData;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.*;

public class RarmorData implements IRarmorData {

    public final BasicInventory modules = new BasicInventory("modules", 3, this);
    private final List<ActiveRarmorModule> loadedModules = new ArrayList<ActiveRarmorModule>();
    private final Map<String, Integer> moduleIdsForSlots = new HashMap<String, Integer>();
    private ItemStack stack;
    private boolean isDirty;

    private int totalTickedTicks;
    private int selectedModule;

    private boolean sentInitialUpdate;

    private boolean isUpdateQueued;
    private boolean queuedUpdateReload;
    private int queuedUpdateConfirmation;

    public RarmorData(ItemStack stack){
        this.stack = stack;
    }

    @Override
    public void readFromNBT(CompoundNBT compound, boolean sync){
        ListNBT data = compound.getList("ModuleData", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < data.size(); i++){
            CompoundNBT tag = data.getCompound(i);

            ActiveRarmorModule module = this.findOrCreateModule(tag.getString("ModuleId"));
            module.readFromNBT(tag, sync);
        }

        this.moduleIdsForSlots.clear();
        ListNBT list = compound.getList("SlotData", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < list.size(); i++){
            CompoundNBT tag = list.getCompound(i);
            String s = tag.getString("ID");
            this.moduleIdsForSlots.put(s == null || s.isEmpty() ? null : s, tag.getInt("Slot"));
        }

        this.selectedModule = compound.getInt("SelectedModule");
        this.totalTickedTicks = compound.getInt("TotalTicks");

        if(sync){
            this.setEnergy(compound.getInt("EnergyStored"));
        }
        else{
            this.modules.loadSlots(compound);
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
    public void sendQueuedUpdate(PlayerEntity player){
        if(this.isUpdateQueued && player instanceof ServerPlayerEntity){
            UUID id = RarmorAPI.methodHandler.checkAndSetRarmorId(this.stack, false);
            PacketHandler.channel.sendTo(new PacketSyncRarmorData(id, this, this.queuedUpdateReload, this.queuedUpdateConfirmation), ((ServerPlayerEntity) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);

            this.isUpdateQueued = false;
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound, boolean sync){
        ListNBT data = new ListNBT();
        for(ActiveRarmorModule module : this.loadedModules){
            CompoundNBT tag = new CompoundNBT();

            module.writeToNBT(tag, sync);
            tag.putString("ModuleId", module.getIdentifier());

            data.add(tag);
        }
        compound.put("ModuleData", data);

        ListNBT list = new ListNBT();
        for(String s : this.moduleIdsForSlots.keySet()){
            CompoundNBT tag = new CompoundNBT();
            tag.putString("ID", s);
            tag.putInt("Slot", this.moduleIdsForSlots.get(s));
            list.add(tag);
        }
        compound.put("SlotData", list);

        compound.putInt("SelectedModule", this.selectedModule);
        compound.putInt("TotalTicks", this.totalTickedTicks);

        if(sync){
            compound.putInt("EnergyStored", this.getEnergyStored());
        }
        else{
            this.modules.saveSlots(compound);
        }
    }

    @Override
    public List<ActiveRarmorModule> getCurrentModules(){
        return this.loadedModules;
    }

    @Override
    public Map<String, Integer> getModuleIdsForSlots(){
        return this.moduleIdsForSlots;
    }

    @Override
    public IInventory getModuleStacks(){
        return this.modules;
    }

    @Override
    public int getSlotForActiveModule(ActiveRarmorModule module){
        String id = module.getIdentifier();
        if(this.moduleIdsForSlots.containsKey(id)){
            return this.moduleIdsForSlots.get(id);
        }
        else{
            return -1;
        }
    }

    @Override
    public List<String> getActiveModulesForSlot(int slot){
        List<String> modules = new ArrayList<String>();
        for(String key : this.moduleIdsForSlots.keySet()){
            int id = this.moduleIdsForSlots.get(key);
            if(id == slot){
                modules.add(key);
            }
        }
        return modules;
    }

    @Override
    public int getSelectedModule(){
        return this.selectedModule;
    }

    @Override
    public void installModule(ItemStack stack, Entity entity, int slotIndex){
        if(stack.getItem() instanceof IRarmorModuleItem){
            String[] moduleIds = ((IRarmorModuleItem)stack.getItem()).getModuleIdentifiers(stack);
            for(String moduleId : moduleIds){
                if(this.getInstalledModuleWithId(moduleId) == null){
                    ActiveRarmorModule module = Helper.initiateModuleById(moduleId, this);
                    if(module != null && !this.loadedModules.contains(module)){
                        module.onInstalled(entity);
                        this.loadedModules.add(module);
                        this.moduleIdsForSlots.put(module.getIdentifier(), slotIndex);

                        this.setDirty();
                    }
                }
            }
        }
    }

    @Override
    public void tick(World world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        if(!world.isRemote){
            if(!this.sentInitialUpdate){
                this.queueUpdate(true);
                this.sentInitialUpdate = true;
            }
        }

        List<ActiveRarmorModule> forUninstall = null;
        for(ActiveRarmorModule module : this.loadedModules){
            if(module != null){
                module.tick(world, entity, isWearingHat, isWearingChest, isWearingPants, isWearingShoes);

                if(module.invalid){
                    if(forUninstall == null){
                        forUninstall = new ArrayList<ActiveRarmorModule>();
                    }
                    forUninstall.add(module);
                }
            }
        }

        if(forUninstall != null){
            for(ActiveRarmorModule module : forUninstall){
                this.uninstallModule(module, entity, true);
            }
            this.queueUpdate(true, -1, true);
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
        if(moduleId != null){
            for(ActiveRarmorModule module : this.loadedModules){
                if(moduleId.equals(module.getIdentifier())){
                    return module;
                }
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
        ItemRarmorChest item = this.getEnergyContainer();
        if(item != null){
            return item.getEnergyStored(this.stack);
        }
        return 0;
    }

    @Override
    public int getMaxEnergyStored(){
        ItemRarmorChest item = this.getEnergyContainer();
        if(item != null){
            return item.getMaxEnergyStored(this.stack);
        }
        return 0;
    }

    @Override
    public int receiveEnergy(int energy, boolean simulate){
        ItemRarmorChest item = this.getEnergyContainer();
        if(item != null){
            return item.receiveEnergy(this.stack, energy, simulate);
        }
        return 0;
    }

    @Override
    public int extractEnergy(int energy, boolean simulate){
        ItemRarmorChest item = this.getEnergyContainer();
        if(item != null){
            return item.extractEnergy(this.stack, energy, simulate);
        }
        return 0;
    }

    @Override
    public void setEnergy(int energy){
        ItemRarmorChest item = this.getEnergyContainer();
        if(item != null){
            Helper.setItemEnergy(this.stack, energy);
        }
    }

    @Override
    public boolean getDirty(){
        return this.isDirty;
    }

    @Override
    public void setDirty(boolean yes){
        this.isDirty = yes;
    }

    @Override
    public void setDirty(){
        this.setDirty(true);
    }

    private ItemRarmorChest getEnergyContainer(){
        if(!stack.isEmpty()){
            Item item = this.stack.getItem();
            if(item instanceof ItemRarmorChest){
                return (ItemRarmorChest)item;
            }
        }
        return null;
    }

    @Override
    public void uninstallModule(ActiveRarmorModule module, Entity entity, boolean drop){
        if(module != null && this.loadedModules.contains(module) && this.getInstalledModuleWithId(module.getIdentifier()) != null){
            module.onUninstalled(entity);
            this.loadedModules.remove(module);

            int slot = this.getSlotForActiveModule(module);
            if(slot >= 0){
                this.moduleIdsForSlots.remove(module.getIdentifier());
                if(drop && !entity.getEntityWorld().isRemote){
                    this.modules.dropSingle(entity, slot);
                }
            }

            this.setDirty();
        }
    }
}
