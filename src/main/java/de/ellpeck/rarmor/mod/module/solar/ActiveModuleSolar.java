/*
 * This file ("ActiveModuleSolar.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.solar;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ActiveModuleSolar extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Solar";
    private static final int ENERGY_PER_TICK = 10;

    public ActiveModuleSolar(IRarmorData data){
        super(data);
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void tick(World world, Entity entity){
        if(RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EntityEquipmentSlot.HEAD) != null){
            if(!world.isRemote){
                BlockPos pos = entity.getPosition();
                if(world.canSeeSky(pos) && world.isDaytime() && !world.isRainingAt(pos)){
                    if(this.data.getMaxEnergyStored()-this.data.getEnergyStored() >= ENERGY_PER_TICK){
                        this.data.receiveEnergy(ENERGY_PER_TICK, false);

                        if(this.data.getTotalTickedTicks()%10 == 0){
                            this.data.queueUpdate();
                        }
                    }
                }
            }
        }
        else{
            this.invalid = true;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean sync){

    }

    @Override
    public void writeToNBT(NBTTagCompound compound, boolean sync){

    }

    @Override
    public RarmorModuleContainer createContainer(EntityPlayer player, Container container){
        return null;
    }

    @Override
    public RarmorModuleGui createGui(GuiContainer gui){
        return null;
    }

    @Override
    public void onInstalled(Entity entity){

    }

    @Override
    public void onUninstalled(Entity entity){

    }

    @Override
    public boolean hasTab(EntityPlayer player){
        return false;
    }

    @Override
    public ItemStack getTabIcon(){
        return new ItemStack(Blocks.DAYLIGHT_DETECTOR_INVERTED);
    }
}
