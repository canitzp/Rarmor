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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ActiveModuleSolar extends ActiveRarmorModule{

    private static final ItemStack DAYLIGHT_SENSOR = new ItemStack(Blocks.DAYLIGHT_DETECTOR);
    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Solar";

    private static final int ENERGY_PER_TICK = 10;

    private boolean generatedLastTick;

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

                        this.generatedLastTick = true;
                        return;
                    }
                }
                this.generatedLastTick = false;
            }
        }
        else{
            this.invalid = true;
        }
    }

    @Override
    public void renderAdditionalOverlay(Minecraft mc, EntityPlayer player, IRarmorData data, ScaledResolution resolution, int renderX, int renderY, float partialTicks){
        renderX += 19;
        renderY += 5;

        FontRenderer font = mc.fontRendererObj;
        boolean unicode = font.getUnicodeFlag();
        font.setUnicodeFlag(true);
        TextFormatting color = this.generatedLastTick ? TextFormatting.GREEN : TextFormatting.RED;
        String key = this.generatedLastTick ? "generating" : "notGenerating";
        font.drawString(color+I18n.format(RarmorAPI.MOD_ID+"."+key), renderX, renderY, 0xFFFFFF, true);
        font.setUnicodeFlag(unicode);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean sync){
        if(sync){
            this.generatedLastTick = compound.getBoolean("GenLastTick");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, boolean sync){
        if(sync){
            compound.setBoolean("GenLastTick", this.generatedLastTick);
        }
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
        return DAYLIGHT_SENSOR;
    }
}
