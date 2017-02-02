/*
 * This file ("ActiveModuleFurnace.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.furnace;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.inventory.gui.BasicInventory;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ActiveModuleFurnace extends ActiveRarmorModule {

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Furnace";
    public static final int TIME_TO_REACH = 150;
    private static final ItemStack FURNACE = new ItemStack(Blocks.FURNACE);
    private static final int ENERGY_PER_TICK = 40;

    public final BasicInventory inventory = new BasicInventory("furnace", 2, this.data);
    public int burnTime;

    public ActiveModuleFurnace(IRarmorData data){
        super(data);
    }

    @Override
    public void tick(World world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        if(!world.isRemote){
            if(this.data.getEnergyStored() >= ENERGY_PER_TICK){
                ItemStack input = this.inventory.getStackInSlot(0);
                if(!input.isEmpty()){
                    ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
                    if(!output.isEmpty()){
                        ItemStack outputSlot = this.inventory.getStackInSlot(1);
                        if(outputSlot.isEmpty() || Helper.canBeStacked(output, outputSlot)){
                            this.burnTime++;
                            this.data.setDirty();

                            this.data.extractEnergy(ENERGY_PER_TICK, false);

                            if(this.burnTime >= TIME_TO_REACH){
                                if(outputSlot.isEmpty()){
                                    this.inventory.setInventorySlotContents(1, output.copy());
                                }
                                else{
                                    outputSlot.grow(output.getCount());
                                }

                                this.inventory.decrStackSize(0, 1);
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
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderAdditionalOverlay(Minecraft mc, EntityPlayer player, IRarmorData data, ScaledResolution resolution, int renderX, int renderY, float partialTicks){
        renderX += 19;
        renderY += 5;

        if(this.burnTime > 0){
            FontRenderer font = mc.fontRenderer;
            String percentage = (int)(((float)this.burnTime/(float)TIME_TO_REACH)*100)+"%";
            boolean unicode = font.getUnicodeFlag();
            font.setUnicodeFlag(true);
            font.drawString(percentage, renderX+15-font.getStringWidth(percentage)/2, renderY-5, 0xFFFFFF, true);
            font.setUnicodeFlag(unicode);
        }

        ItemStack input = this.inventory.getStackInSlot(0);
        if(!input.isEmpty()){
            Helper.renderStackToGui(input, renderX, renderY, 0.7F);
            renderX += 18;
        }
        Helper.renderStackToGui(this.inventory.getStackInSlot(1), renderX, renderY, 0.7F);
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean sync){
        this.inventory.loadSlots(compound);
        this.burnTime = compound.getInteger("BurnTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, boolean sync){
        this.inventory.saveSlots(compound);
        compound.setInteger("BurnTime", this.burnTime);
    }

    @Override
    public RarmorModuleContainer createContainer(EntityPlayer player, Container container){
        return new ContainerModuleFurnace(player, container, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RarmorModuleGui createGui(GuiContainer gui){
        return new GuiModuleFurnace(gui, this);
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
        return FURNACE;
    }

}
