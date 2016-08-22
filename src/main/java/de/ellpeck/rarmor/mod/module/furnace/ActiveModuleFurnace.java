/*
 * This file ("ActiveModuleEnder.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.furnace;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.inventory.gui.BasicInventory;
import de.ellpeck.rarmor.mod.misc.Helper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ActiveModuleFurnace extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Furnace";
    public static final int TIME_TO_REACH = 150;
    private static final int ENERGY_PER_TICK = 40;

    public final BasicInventory inventory = new BasicInventory("furnace", 2);
    public int burnTime;

    public ActiveModuleFurnace(IRarmorData data){
        super(data);
    }

    @Override
    public void tick(World world, Entity entity){
        if(!world.isRemote){
            if(this.data.getEnergyStored() >= ENERGY_PER_TICK){
                ItemStack input = this.inventory.getStackInSlot(0);
                if(input != null){
                    ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
                    if(output != null){
                        ItemStack outputSlot = this.inventory.getStackInSlot(1);
                        if(outputSlot == null || Helper.canBeStacked(output, outputSlot)){
                            this.burnTime++;
                            this.data.extractEnergy(ENERGY_PER_TICK, false);

                            if(this.burnTime >= TIME_TO_REACH){
                                if(outputSlot == null){
                                    this.inventory.setInventorySlotContents(1, output.copy());
                                }
                                else{
                                    outputSlot.stackSize += output.stackSize;
                                }

                                this.inventory.decrStackSize(0, 1);
                                this.burnTime = 0;
                            }

                            if(this.burnTime%15 == 0){
                                this.data.queueUpdate();
                            }

                            return;
                        }
                    }
                }
                this.burnTime = 0;
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
        this.burnTime = compound.getInteger("BurnTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, boolean sync){
        if(!sync){
            this.inventory.saveSlots(compound);
        }
        compound.setInteger("BurnTime", this.burnTime);
    }

    @Override
    public RarmorModuleContainer createContainer(EntityPlayer player, Container container){
        return new ContainerModuleFurnace(player, container, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RarmorModuleGui createGui(GuiContainer gui){
        return new GuiModuleFurnace(gui, this);
    }

    @Override
    public void onInstalled(EntityPlayer player){

    }

    @Override
    public void onUninstalled(EntityPlayer player){
        this.inventory.drop(player);
    }

    @Override
    public boolean hasTab(EntityPlayer player){
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIcon(){
        return new ItemStack(Blocks.FURNACE);
    }

}
