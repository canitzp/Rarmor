/*
 * This file ("ActiveModuleGenerator.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.generator;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.item.RarmorItemRegistry;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.inventory.gui.BasicInventory;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ActiveModuleGenerator extends ActiveRarmorModule {

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Generator";
    private static final ItemStack GENERATOR = new ItemStack(RarmorItemRegistry.itemGenerator.get());
    private static final int ENERGY_PER_TICK = 30;

    public final BasicInventory inventory = new BasicInventory("input", 1, this.data);

    public int currentBurnTime;
    public int burnTimeTickingDownFrom;

    public ActiveModuleGenerator(IRarmorData data){
        super(data);
    }

    @Override
    public void tick(World world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        if(!world.isRemote){
            boolean canAddEnergy = this.data.getMaxEnergyStored()-this.data.getEnergyStored() >= ENERGY_PER_TICK;

            if(this.currentBurnTime > 0){
                this.currentBurnTime--;
                this.data.setDirty();

                if(canAddEnergy){
                    this.data.receiveEnergy(ENERGY_PER_TICK, false);
                }

                if(this.data.getTotalTickedTicks()%10 == 0){
                    this.data.queueUpdate();
                }
            }
            else if(canAddEnergy){
                ItemStack stack = this.inventory.getStackInSlot(0);
                if(!stack.isEmpty()){
                    int time = stack.getBurnTime();
                    if(time > 0){
                        this.currentBurnTime = time;
                        this.burnTimeTickingDownFrom = time;
                        this.data.setDirty();

                        stack.shrink(1);
                    }
                }
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderAdditionalOverlay(MatrixStack matrixStack, Minecraft mc, PlayerEntity player, IRarmorData data, MainWindow window, int renderX, int renderY, float partialTicks){
        renderX += 19;
        renderY += 2;
        Helper.renderStackToGui(matrixStack, this.inventory.getStackInSlot(0), renderX, renderY, 0.7F);
    
        renderX += 20;
        if(this.currentBurnTime > 0 && this.burnTimeTickingDownFrom > 0){
            FontRenderer font = mc.fontRenderer;
            String percentage = (int)(((float)this.currentBurnTime/(float)this.burnTimeTickingDownFrom)*100)+"%";
            font.drawString(matrixStack, percentage, renderX-font.getStringWidth(percentage)/2, renderY, 0xFFFFFF);
        }
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void readFromNBT(CompoundNBT compound, boolean sync){
        this.inventory.loadSlots(compound);
        this.currentBurnTime = compound.getInt("BurnTime");
        this.burnTimeTickingDownFrom = compound.getInt("BurnTimeFrom");
    }

    @Override
    public void writeToNBT(CompoundNBT compound, boolean sync){
        this.inventory.saveSlots(compound);
        compound.putInt("BurnTime", this.currentBurnTime);
        compound.putInt("BurnTimeFrom", this.burnTimeTickingDownFrom);
    }

    @Override
    public RarmorModuleContainer createContainer(PlayerEntity player, Container container){
        return new ContainerModuleGenerator(container, this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RarmorModuleGui createGui(){
        return new GuiModuleGenerator(this);
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
        return GENERATOR;
    }
}
