/*
 * This file ("ActiveModuleMain.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.main;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.item.ItemRegistry;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.inventory.gui.BasicInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ActiveModuleMain extends ActiveRarmorModule{

    public static final int MODULE_SLOT_AMOUNT = 3;
    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Main";
    private static final ItemStack CHESTPLATE = new ItemStack(ItemRegistry.itemRarmorChest, 1, 0);
    public final BasicInventory inventory = new BasicInventory("main", 2, this.data);
    private int lastEnergy;

    public ActiveModuleMain(IRarmorData data){
        super(data);
    }

    @Override
    public void tick(World world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        if(!world.isRemote){
            if(this.data.getEnergyStored() < this.data.getMaxEnergyStored()){
                ItemStack discharge = this.inventory.getStackInSlot(0);
                if(!discharge.isEmpty()){
                    if(discharge.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN)){
                        IEnergyStorage storage = discharge.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN);
                        if(storage != null){
                            int canDischarge = storage.extractEnergy(Integer.MAX_VALUE, true);
                            if(canDischarge > 0){
                                int discharged = this.data.receiveEnergy(canDischarge, false);
                                storage.extractEnergy(discharged, false);
                                this.data.setDirty();
                            }
                        }
                    }
                }
            }

            if(this.data.getEnergyStored() > 0){
                ItemStack charge = this.inventory.getStackInSlot(1);
                if(!charge.isEmpty()){
                    if(charge.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN)){
                        IEnergyStorage storage = charge.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN);
                        if(storage != null){
                            int canDischarge = storage.receiveEnergy(Integer.MAX_VALUE, true);
                            if(canDischarge > 0){
                                int discharged = this.data.extractEnergy(canDischarge, false);
                                storage.receiveEnergy(discharged, false);
                                this.data.setDirty();
                            }
                        }
                    }
                }
            }

            int energy = this.data.getEnergyStored();
            if(energy != this.lastEnergy && this.data.getTotalTickedTicks()%10 == 0){
                this.data.queueUpdate();
                this.lastEnergy = energy;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderAdditionalOverlay(Minecraft mc, EntityPlayer player, IRarmorData data, ScaledResolution resolution, int renderX, int renderY, float partialTicks){

    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean sync){
        if(!sync){
            this.inventory.loadSlots(compound);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, boolean sync){
        if(!sync){
            this.inventory.saveSlots(compound);
        }
    }

    @Override
    public RarmorModuleContainer createContainer(EntityPlayer player, Container container){
        return new ContainerModuleMain(player, container, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RarmorModuleGui createGui(GuiContainer container){
        return new GuiModuleMain(container, this);
    }

    @Override
    public void onInstalled(Entity entity){
        //Called with null player for this module
    }

    @Override
    public void onUninstalled(Entity entity){
        //Not called for this module
    }

    @Override
    public boolean hasTab(EntityPlayer player){
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getDisplayIcon(){
        return CHESTPLATE;
    }

    @Override
    public boolean doesRenderOnOverlay(Minecraft mc, EntityPlayer player, IRarmorData data){
        return false;
    }
}
