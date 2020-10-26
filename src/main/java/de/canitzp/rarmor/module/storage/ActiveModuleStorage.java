/*
 * This file ("ActiveModuleStorage.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.storage;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.inventory.gui.BasicInventory;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ActiveModuleStorage extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Storage";
    private static final ItemStack CHEST = new ItemStack(Blocks.CHEST);

    public final BasicInventory inventory = new BasicInventory("storage", 46, this.data);

    public ActiveModuleStorage(IRarmorData data){
        super(data);
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void readFromNBT(CompoundNBT compound, boolean sync){
        if(!sync){
            this.inventory.loadSlots(compound);
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound, boolean sync){
        if(!sync){
            this.inventory.saveSlots(compound);
        }
    }

    @Override
    public RarmorModuleContainer createContainer(PlayerEntity player, Container container){
        return new ContainerModuleStorage(player, container, this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RarmorModuleGui createGui(){
        return new GuiModuleStorage(this);
    }

    @Override
    public void onUninstalled(Entity entity){
        this.inventory.drop(entity);
    }

    @Override
    public boolean hasTab(PlayerEntity player){
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack getDisplayIcon(){
        return CHEST;
    }

}
