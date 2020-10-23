/*
 * This file ("ActiveModuleProtection.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.protection;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public abstract class ActiveModuleProtection extends ActiveRarmorModule {

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
    public void readFromNBT(CompoundNBT compound, boolean sync){

    }

    @Override
    public void writeToNBT(CompoundNBT compound, boolean sync){

    }

    @Override
    public RarmorModuleContainer createContainer(PlayerEntity player, Container container){
        return null;
    }
    
    @Override
    public <T extends Container> RarmorModuleGui<T> createGui(ContainerScreen<T> gui){
        return null;
    }
    
    @Override
    public void onInstalled(Entity entity){

    }

    @Override
    public void onUninstalled(Entity entity){

    }

    @Override
    public boolean hasTab(PlayerEntity player){
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
    public void renderAdditionalOverlay(Minecraft mc, PlayerEntity player, IRarmorData data, MainWindow resolution, int renderX, int renderY, float partialTicks){

    }

    public abstract float getDamageReduction();
}
