/*
 * This file ("ActiveModuleSolar.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.solar;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.item.RarmorItemRegistry;
import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    public void tick(Level world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        if(isWearingHat){
            if(!world.isClientSide()){
                BlockPos pos = new BlockPos(entity.getX(), entity.getY()+entity.getEyeHeight(), entity.getZ());
                if(world.canSeeSky(pos) && world.isDay() && !world.isRainingAt(pos)){
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
    public void renderAdditionalOverlay(PoseStack matrixStack, Minecraft mc, Player player, IRarmorData data, Window window, int renderX, int renderY, float partialTicks){
        renderX += 19;
        renderY += 5;
    
        Font font = mc.font;
        ChatFormatting color = this.generatedLastTick ? ChatFormatting.GREEN : ChatFormatting.RED;
        String key = this.generatedLastTick ? "generating" : "notGenerating";
        if(data.getEnergyStored() == data.getMaxEnergyStored()){
            color = ChatFormatting.BLUE;
            key = "full";
        }
        font.draw(matrixStack, color+ I18n.get(RarmorAPI.MOD_ID+"."+key), renderX, renderY, 0xFFFFFF);
    }
    
    @Override
    public void readFromNBT(CompoundTag compound, boolean sync){
        if(sync){
            this.generatedLastTick = compound.getBoolean("GenLastTick");
        }
    }

    @Override
    public void writeToNBT(CompoundTag compound, boolean sync){
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
