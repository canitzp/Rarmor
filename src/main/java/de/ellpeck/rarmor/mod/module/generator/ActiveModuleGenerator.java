/*
 * This file ("ActiveModuleGenerator.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.generator;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.inventory.gui.BasicInventory;
import de.ellpeck.rarmor.mod.item.ItemRegistry;
import de.ellpeck.rarmor.mod.misc.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ActiveModuleGenerator extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Generator";
    private static final ItemStack GENERATOR = new ItemStack(ItemRegistry.itemGenerator);
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
                if(stack != null){
                    int time = TileEntityFurnace.getItemBurnTime(stack);
                    if(time > 0){
                        this.currentBurnTime = time;
                        this.burnTimeTickingDownFrom = time;
                        this.data.setDirty();

                        stack.stackSize--;

                        if(stack.stackSize <= 0){
                            this.inventory.setInventorySlotContents(0, stack.getItem().getContainerItem(stack));
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderAdditionalOverlay(Minecraft mc, EntityPlayer player, IRarmorData data, ScaledResolution resolution, int renderX, int renderY, float partialTicks){
        renderX += 19;
        renderY += 2;
        Helper.renderStackToGui(this.inventory.getStackInSlot(0), renderX, renderY, 0.7F);

        renderX += 20;
        if(this.currentBurnTime > 0 && this.burnTimeTickingDownFrom > 0){
            FontRenderer font = mc.fontRendererObj;
            String percentage = (int)(((float)this.currentBurnTime/(float)this.burnTimeTickingDownFrom)*100)+"%";
            boolean unicode = font.getUnicodeFlag();
            font.setUnicodeFlag(true);
            font.drawString(percentage, renderX-font.getStringWidth(percentage)/2, renderY, 0xFFFFFF, true);
            font.setUnicodeFlag(unicode);
        }
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean sync){
        this.inventory.loadSlots(compound);
        this.currentBurnTime = compound.getInteger("BurnTime");
        this.burnTimeTickingDownFrom = compound.getInteger("BurnTimeFrom");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, boolean sync){
        this.inventory.saveSlots(compound);
        compound.setInteger("BurnTime", this.currentBurnTime);
        compound.setInteger("BurnTimeFrom", this.burnTimeTickingDownFrom);
    }

    @Override
    public RarmorModuleContainer createContainer(EntityPlayer player, Container container){
        return new ContainerModuleGenerator(container, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public RarmorModuleGui createGui(GuiContainer gui){
        return new GuiModuleGenerator(gui, this);
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

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getDisplayIcon(){
        return GENERATOR;
    }
}
