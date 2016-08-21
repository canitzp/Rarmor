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
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ActiveModuleMain extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Main";

    public final BasicInventory inventory = new BasicInventory("main", 5);

    public ActiveModuleMain(IRarmorData data){
        super(data);
    }

    @Override
    public void tick(World world){
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
                        this.data.queueUpdate();
                    }
                }
                else if(Compat.teslaLoaded && discharge.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, EnumFacing.DOWN)){
                    ITeslaProducer cap = discharge.getCapability(TeslaCapabilities.CAPABILITY_PRODUCER, EnumFacing.DOWN);
                    if(cap != null){
                        int canDischarge = (int)cap.takePower(Integer.MAX_VALUE, true);
                        if(canDischarge > 0){
                            int discharged = this.data.receiveEnergy(canDischarge, false);
                            cap.takePower(discharged, false);
                            this.data.queueUpdate();
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
                        this.data.queueUpdate();
                    }
                }
                else if(Compat.teslaLoaded && charge.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN)){
                    ITeslaConsumer cap = charge.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.DOWN);
                    if(cap != null){
                        int canCharge = (int)cap.givePower(Integer.MAX_VALUE, true);
                        if(canCharge > 0){
                            int charged = this.data.extractEnergy(canCharge, false);
                            cap.givePower(charged, false);
                            this.data.queueUpdate();
                        }
                    }
                }
            }
        }
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
    public void onInstalled(EntityPlayer player){
        //Called with null player for this module
    }

    @Override
    public void onUninstalled(EntityPlayer player){
        //Not called for this module
    }

    @Override
    public boolean hasTab(EntityPlayer player){
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIcon(){
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player != null){
            String name = player.getName();
            if("bootytoast".equalsIgnoreCase(name)){
                return new ItemStack(Items.BREAD);
            }
            else if("canitzp".equalsIgnoreCase(name)){
                return new ItemStack(Items.CARROT_ON_A_STICK);
            }
            else if("ellpeck".equalsIgnoreCase(name)){
                return new ItemStack(Items.REDSTONE);
            }
        }

        return new ItemStack(Blocks.CRAFTING_TABLE);
    }
}
