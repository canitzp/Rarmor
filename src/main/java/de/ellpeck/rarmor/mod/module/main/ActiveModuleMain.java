/*
 * This file ("ActiveModuleMain.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.main;

import cofh.api.energy.IEnergyContainerItem;
import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.compat.Compat;
import de.ellpeck.rarmor.mod.inventory.gui.BasicInventory;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ActiveModuleMain extends ActiveRarmorModule{

    public static final int MODULE_SLOT_AMOUNT = 3;
    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Main";
    private static final ItemStack CRAFTING_TABLE = new ItemStack(Blocks.CRAFTING_TABLE);
    public final BasicInventory inventory = new BasicInventory("main", 2, this.data);
    private int lastEnergy;

    public ActiveModuleMain(IRarmorData data){
        super(data);
    }

    @Override
    public void tick(World world, Entity entity){
        if(!world.isRemote){
            if(this.data.getEnergyStored() < this.data.getMaxEnergyStored()){
                ItemStack discharge = this.inventory.getStackInSlot(0);
                if(discharge != null){
                    Item item = discharge.getItem();
                    if(item instanceof IEnergyContainerItem){
                        IEnergyContainerItem container = (IEnergyContainerItem)item;
                        int canDischarge = container.extractEnergy(discharge, Integer.MAX_VALUE, true);
                        if(canDischarge > 0){
                            int discharged = this.data.receiveEnergy(canDischarge, false);
                            container.extractEnergy(discharge, discharged, false);
                            this.data.setDirty();
                        }
                    }
                    else if(Compat.teslaLoaded && discharge.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, EnumFacing.DOWN)){
                        ITeslaProducer cap = discharge.getCapability(TeslaCapabilities.CAPABILITY_PRODUCER, EnumFacing.DOWN);
                        if(cap != null){
                            int canDischarge = (int)cap.takePower(Integer.MAX_VALUE, true);
                            if(canDischarge > 0){
                                int discharged = this.data.receiveEnergy(canDischarge, false);
                                cap.takePower(discharged, false);
                                this.data.setDirty();
                            }
                        }
                    }
                }
            }

            if(this.data.getEnergyStored() > 0){
                ItemStack charge = this.inventory.getStackInSlot(1);
                if(charge != null){
                    Item item = charge.getItem();
                    if(item instanceof IEnergyContainerItem){
                        IEnergyContainerItem container = (IEnergyContainerItem)item;
                        int canCharge = container.receiveEnergy(charge, Integer.MAX_VALUE, true);
                        if(canCharge > 0){
                            int charged = this.data.extractEnergy(canCharge, false);
                            container.receiveEnergy(charge, charged, false);
                            this.data.setDirty();
                        }
                    }
                    else if(Compat.teslaLoaded && charge.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN)){
                        ITeslaConsumer cap = charge.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN);
                        if(cap != null){
                            int canCharge = (int)cap.givePower(Integer.MAX_VALUE, true);
                            if(canCharge > 0){
                                int charged = this.data.extractEnergy(canCharge, false);
                                cap.givePower(charged, false);
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
        return CRAFTING_TABLE;
    }

    @Override
    public boolean doesRenderOnOverlay(Minecraft mc, EntityPlayer player, IRarmorData data){
        return false;
    }
}
