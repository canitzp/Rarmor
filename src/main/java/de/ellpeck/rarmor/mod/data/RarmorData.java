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
import de.ellpeck.rarmor.mod.inventory.gui.BasicInventory;
import de.ellpeck.rarmor.mod.item.ItemRarmorChest;
import de.ellpeck.rarmor.mod.misc.Helper;
import de.ellpeck.rarmor.mod.packet.PacketHandler;
import de.ellpeck.rarmor.mod.packet.PacketSyncRarmorData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RarmorData implements IRarmorData{

    public final BasicInventory modules = new BasicInventory("modules", 3, this);
    private final List<ActiveRarmorModule> loadedModules = new ArrayList<ActiveRarmorModule>();
    private final String[] moduleIdsForSlots = new String[3];
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
    public void readFromNBT(NBTTagCompound compound, boolean sync){
        NBTTagList data = compound.getTagList("ModuleData", 10);
        for(int i = 0; i < data.tagCount(); i++){
            NBTTagCompound tag = data.getCompoundTagAt(i);

            ActiveRarmorModule module = this.findOrCreateModule(tag.getString("ModuleId"));
            module.readFromNBT(tag, sync);
        }

        NBTTagList list = compound.getTagList("SlotData", 8);
        for(int i = 0; i < list.tagCount(); i++){
            String s = list.getStringTagAt(i);
            this.moduleIdsForSlots[i] = s == null || s.isEmpty() ? null : s;
        }

        this.selectedModule = compound.getInteger("SelectedModule");
        this.totalTickedTicks = compound.getInteger("TotalTicks");

        if(sync){
            this.setEnergy(compound.getInteger("EnergyStored"));
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
        for(String id : this.moduleIdsForSlots){
            list.appendTag(new NBTTagString(id == null ? "" : id));
        }
        compound.setTag("SlotData", list);

        compound.setInteger("SelectedModule", this.selectedModule);
        compound.setInteger("TotalTicks", this.totalTickedTicks);

        if(sync){
            compound.setInteger("EnergyStored", this.getEnergyStored());
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
    public String[] getModulesForSlotsArray(){
        return this.moduleIdsForSlots;
    }

    @Override
    public IInventory getModuleStacks(){
        return this.modules;
    }

    @Override
    public int getSlotForActiveModule(ActiveRarmorModule module){
        for(int i = 0; i < this.moduleIdsForSlots.length; i++){
            if(module.getIdentifier().equals(this.moduleIdsForSlots[i])){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSelectedModule(){
        return this.selectedModule;
    }

    @Override
    public void installModule(ItemStack stack, Entity entity, int slotIndex){
        if(stack.getItem() instanceof IRarmorModuleItem){
            String moduleId = ((IRarmorModuleItem)stack.getItem()).getModuleIdentifier(stack);
            if(this.getInstalledModuleWithId(moduleId) == null){
                ActiveRarmorModule module = Helper.initiateModuleById(moduleId, this);
                if(module != null && !this.loadedModules.contains(module)){
                    module.onInstalled(entity);
                    this.loadedModules.add(module);
                    this.moduleIdsForSlots[slotIndex] = module.getIdentifier();

                    this.setDirty();
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
        if(this.stack != null){
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
                this.moduleIdsForSlots[slot] = null;
                if(drop && !entity.worldObj.isRemote){
                    this.modules.dropSingle(entity, slot);
                }
            }

            this.setDirty();
        }
    }
}
