/*
 * This file ("ActiveModuleProtection.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.protection;

import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class ActiveModuleProtection extends ActiveRarmorModule{

    private final String identifier;
    private final ItemStack display;

    public ActiveModuleProtection(ItemStack display, String identifier, IRarmorData data){
        super(data);
        this.identifier = identifier;
        this.display = display;
    }

    @Override
    public String getIdentifier(){
        return this.identifier;
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
        return this.display;
    }

    @Override
    public void tick(World world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){

    }

    @Override
    public void renderAdditionalOverlay(Minecraft mc, EntityPlayer player, IRarmorData data, ScaledResolution resolution, int renderX, int renderY, float partialTicks){

    }

    public abstract float getDamageReduction();
}
