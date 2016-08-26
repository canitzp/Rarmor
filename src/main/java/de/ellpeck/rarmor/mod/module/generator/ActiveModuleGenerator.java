/*
 * This file ("ActiveModuleGenerator.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.generator;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.inventory.gui.BasicInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class ActiveModuleGenerator extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Generator";
    private static final int ENERGY_PER_TICK = 30;

    public final BasicInventory inventory = new BasicInventory("input", 1);

    public int currentBurnTime;
    public int burnTimeTickingDownFrom;

    public ActiveModuleGenerator(IRarmorData data){
        super(data);
    }

    @Override
    public void tick(World world, Entity entity){
        if(!world.isRemote){
            boolean canAddEnergy = this.data.getMaxEnergyStored()-this.data.getEnergyStored() >= ENERGY_PER_TICK;

            if(this.currentBurnTime > 0){
                this.currentBurnTime--;

                if(canAddEnergy){
                    this.data.receiveEnergy(ENERGY_PER_TICK, false);
                }

                if(this.data.getTotalTickedTicks()%10 == 0){
                    this.data.queueUpdate();
                }
            }
            else if(canAddEnergy){
                ItemStack stack = this.inventory.getStackInSlot(0);
                if(stack != null){
                    int time = TileEntityFurnace.getItemBurnTime(stack);
                    if(time > 0){
                        this.currentBurnTime = time;
                        this.burnTimeTickingDownFrom = time;

                        stack.stackSize--;

                        if(stack.stackSize <= 0){
                            this.inventory.setInventorySlotContents(0, stack.getItem().getContainerItem(stack));
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

        this.currentBurnTime = compound.getInteger("BurnTime");
        this.burnTimeTickingDownFrom = compound.getInteger("BurnTimeFrom");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, boolean sync){
        if(!sync){
            this.inventory.saveSlots(compound);
        }

        compound.setInteger("BurnTime", this.currentBurnTime);
        compound.setInteger("BurnTimeFrom", this.burnTimeTickingDownFrom);
    }

    @Override
    public RarmorModuleContainer createContainer(EntityPlayer player, Container container){
        return new ContainerModuleGenerator(container, this);
    }

    @Override
    public RarmorModuleGui createGui(GuiContainer gui){
        return new GuiModuleGenerator(gui, this);
    }

    @Override
    public void onInstalled(Entity entity){

    }

    @Override
    public void onUninstalled(Entity entity){
        this.inventory.drop(entity);
    }

    @Override
    public boolean hasTab(EntityPlayer player){
        return true;
    }

    @Override
    public ItemStack getTabIcon(){
        return new ItemStack(Blocks.FURNACE);
    }
}
