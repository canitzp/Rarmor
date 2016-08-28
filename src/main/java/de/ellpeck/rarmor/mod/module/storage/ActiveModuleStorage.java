/*
 * This file ("ActiveModuleStorage.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.storage;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.inventory.gui.BasicInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ActiveModuleStorage extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Storage";
    private static final ItemStack CHEST = new ItemStack(Blocks.CHEST);

    public final BasicInventory inventory = new BasicInventory("storage", 46);

    public ActiveModuleStorage(IRarmorData data){
        super(data);
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
        return new ContainerModuleStorage(player, container, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RarmorModuleGui createGui(GuiContainer gui){
        return new GuiModuleStorage(gui, this);
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
    @SideOnly(Side.CLIENT)
    public ItemStack getDisplayIcon(){
        return CHEST;
    }

    @Override
    public void tick(World world, Entity entity){

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderAdditionalOverlay(Minecraft mc, EntityPlayer player, IRarmorData data, ScaledResolution resolution, int renderX, int renderY, float partialTicks){

    }
}
