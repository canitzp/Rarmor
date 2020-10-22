/*
 * This file ("ActiveModuleSpeed.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.speed;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ActiveModuleSpeed extends ActiveRarmorModule {

    private static final ItemStack BOOTS = new ItemStack(Items.GOLDEN_BOOTS);
    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Speed";

    private double lastPlayerX;
    private double lastPlayerZ;

    public ActiveModuleSpeed(IRarmorData data){
        super(data);
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
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
    public ItemStack getDisplayIcon(){
        return BOOTS;
    }

    @Override
    public void tick(World world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        if(isWearingChest && isWearingShoes){
            int use = 1;
            if(this.data.getEnergyStored() >= use){
                if(entity instanceof EntityPlayer){
                    EntityPlayer player = (EntityPlayer)entity;
                    if((player.onGround || player.capabilities.isFlying) && !player.isInsideOfMaterial(Material.WATER)){
                        if(world.isRemote){
                            if(player.moveForward > 0){
                                player.moveRelative(0F, 1F, 0.075F, 0F); // TODO this shit doesn't work
                            }
                        }
                        else{
                            if(this.lastPlayerX != player.posX || this.lastPlayerZ != player.posZ){
                                if(this.data.getTotalTickedTicks()%5 == 0){
                                    this.data.extractEnergy(use, false);
                                }

                                this.lastPlayerX = player.posX;
                                this.lastPlayerZ = player.posZ;
                            }
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
    public void renderAdditionalOverlay(Minecraft mc, EntityPlayer player, IRarmorData data, ScaledResolution resolution, int renderX, int renderY, float partialTicks){

    }
}
