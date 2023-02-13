/*
 * This file ("ActiveModuleFurnace.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.furnace;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.inventory.gui.BasicInventory;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ActiveModuleFurnace extends ActiveRarmorModule {

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Furnace";
    public static final int TIME_TO_REACH = 150;
    private static final ItemStack FURNACE = new ItemStack(Blocks.FURNACE);
    private static final int ENERGY_PER_TICK = 40;

    public final BasicInventory inventory = new BasicInventory( 2, this.data);
    public int burnTime;

    public ActiveModuleFurnace(IRarmorData data){
        super(data);
    }

    @Override
    public void tick(Level world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        /*if(!world.isClientSide()){
            if(this.data.getEnergyStored() >= ENERGY_PER_TICK){
                SmeltingRecipe furnaceRecipe = world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, this.inventory, world).orElse(null);
                if(furnaceRecipe != null){
                    ItemStack output = furnaceRecipe.getResultItem();
                    if(!output.isEmpty()){
                        ItemStack outputSlot = this.inventory.getStackInSlot(1);
                        if(outputSlot.isEmpty() || Helper.canBeStacked(output, outputSlot)){
                            this.burnTime++;
                            this.data.setDirty();
        
                            this.data.extractEnergy(ENERGY_PER_TICK, false);
        
                            if(this.burnTime >= TIME_TO_REACH){
                                if(outputSlot.isEmpty()){
                                    this.inventory.setStackInSlot(1, output.copy());
                                }
                                else{
                                    outputSlot.grow(output.getCount());
                                }
            
                                this.inventory.getStackInSlot(0).shrink(1);
                                this.burnTime = 0;
                            }
        
                            if(this.data.getTotalTickedTicks()%10 == 0){
                                this.data.queueUpdate();
                            }
        
                            return;
                        }
                    }
                }
                this.burnTime = 0;
                this.data.setDirty();
            }
        }*/
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderAdditionalOverlay(PoseStack matrixStack, Minecraft mc, Player player, IRarmorData data, Window window, int renderX, int renderY, float partialTicks){
        renderX += 19;
        renderY += 5;
        
        if(this.burnTime > 0){
            Font font = mc.font;
            String percentage = (int)(((float)this.burnTime/(float)TIME_TO_REACH)*100)+"%";
            font.draw(matrixStack, percentage, renderX+15-font.width(percentage)/2, renderY-5, 0xFFFFFF);
        }
    
        ItemStack input = this.inventory.getStackInSlot(0);
        if(!input.isEmpty()){
            Helper.renderStackToGui(matrixStack, input, renderX, renderY, 0.7F);
            renderX += 18;
        }
        Helper.renderStackToGui(matrixStack, this.inventory.getStackInSlot(1), renderX, renderY, 0.7F);
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void readFromNBT(CompoundTag compound, boolean sync){
        this.inventory.deserializeNBT(compound.getCompound("Items"));
        this.burnTime = compound.getInt("BurnTime");
    }

    @Override
    public void writeToNBT(CompoundTag compound, boolean sync){
        compound.put("Items", this.inventory.serializeNBT());
        compound.putInt("BurnTime", this.burnTime);
    }

    @Override
    public RarmorModuleContainer createContainer(Player player, AbstractContainerMenu container){
        return new ContainerModuleFurnace(player, container, this);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public RarmorModuleGui createGui(){
        return new GuiModuleFurnace(this);
    }

    @Override
    public void onUninstalled(Entity entity){
        this.inventory.drop(entity);
    }

    @Override
    public boolean hasTab(Player player){
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack getDisplayIcon(){
        return FURNACE;
    }

}
