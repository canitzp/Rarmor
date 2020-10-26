/*
 * This file ("ActiveModuleSolar.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.solar;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.item.RarmorItemRegistry;
import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ActiveModuleSolar extends ActiveRarmorModule {

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Solar";
    private static final ItemStack SOLAR_CELL = new ItemStack(RarmorItemRegistry.itemSolarCell.get());
    private static final int ENERGY_PER_TICK = 15;

    private boolean generatedLastTick;

    public ActiveModuleSolar(IRarmorData data){
        super(data);
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void tick(World world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        if(isWearingHat){
            if(!world.isRemote){
                BlockPos pos = new BlockPos(entity.getPosX(), entity.getPosY()+entity.getHeight(), entity.getPosZ());
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
    public void renderAdditionalOverlay(MatrixStack matrixStack, Minecraft mc, PlayerEntity player, IRarmorData data, MainWindow window, int renderX, int renderY, float partialTicks){
        renderX += 19;
        renderY += 5;
    
        FontRenderer font = mc.fontRenderer;
        TextFormatting color = this.generatedLastTick ? TextFormatting.GREEN : TextFormatting.RED;
        String key = this.generatedLastTick ? "generating" : "notGenerating";
        if(data.getEnergyStored() == data.getMaxEnergyStored()){
            color = TextFormatting.BLUE;
            key = "full";
        }
        font.drawString(matrixStack, color+I18n.format(RarmorAPI.MOD_ID+"."+key), renderX, renderY, 0xFFFFFF);
    }
    
    @Override
    public void readFromNBT(CompoundNBT compound, boolean sync){
        if(sync){
            this.generatedLastTick = compound.getBoolean("GenLastTick");
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound, boolean sync){
        if(sync){
            compound.putBoolean("GenLastTick", this.generatedLastTick);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack getDisplayIcon(){
        return SOLAR_CELL;
    }
}
