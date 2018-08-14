/*
 * This file ("ItemRarmorChest.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemRarmorChest extends ItemRarmor{

    public static final int CAPACITY = 300000;
    private static final int MAX_RECEIVE = 1000;
    private static final int MAX_EXTRACT = 1000;

    public ItemRarmorChest(String name){
        super(name, EntityEquipmentSlot.CHEST);
        this.setHasSubtypes(true);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
        IRarmorData data = RarmorAPI.methodHandler.getDataForStack(world, stack, !world.isRemote);
        if(data != null){
            if(!world.isRemote && entity instanceof EntityPlayer){
                data.sendQueuedUpdate((EntityPlayer)entity);
            }

            boolean hat = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EntityEquipmentSlot.HEAD) != ItemStack.EMPTY;
            boolean chest = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EntityEquipmentSlot.CHEST) != ItemStack.EMPTY;
            boolean pants = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EntityEquipmentSlot.LEGS) != ItemStack.EMPTY;
            boolean shoes = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EntityEquipmentSlot.FEET) != ItemStack.EMPTY;
            data.tick(world, entity, hat, chest, pants, shoes);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
        if(worldIn != null){
            String s = "   ";

            tooltip.add(TextFormatting.GOLD+I18n.format(RarmorAPI.MOD_ID+".storedEnergy")+":");
            tooltip.add(TextFormatting.YELLOW+s+this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack));

            IRarmorData data = RarmorAPI.methodHandler.getDataForStack(worldIn, stack, false);
            if(data != null){
                tooltip.add(TextFormatting.GOLD+I18n.format(RarmorAPI.MOD_ID+".installedModules")+":");
                for(ActiveRarmorModule module : data.getCurrentModules()){
                    tooltip.add(TextFormatting.YELLOW+s+"-"+I18n.format("module."+module.getIdentifier()+".name"));
                }

                tooltip.add(TextFormatting.GOLD+I18n.format(RarmorAPI.MOD_ID+".stackId")+":");
                tooltip.add(TextFormatting.YELLOW+s+RarmorAPI.methodHandler.checkAndSetRarmorId(stack, false));
            }
            else{
                tooltip.add(TextFormatting.RED+""+TextFormatting.ITALIC+I18n.format(RarmorAPI.MOD_ID+".noDataYet"));
            }
        }
    }

    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate){
        if(!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound compound = stack.getTagCompound();

        int energy = compound.getInteger("Energy");
        int energyReceived = Math.min(CAPACITY-energy, Math.min(MAX_RECEIVE, maxReceive));

        if(!simulate){
            energy += energyReceived;
            compound.setInteger("Energy", energy);
        }

        return energyReceived;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack){
        return stack.getItemDamage() != 1;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        if(stack.getItemDamage() != 1){
            double max = this.getMaxEnergyStored(stack);
            return (max-this.getEnergyStored(stack))/max;
        }
        else{
            return super.getDurabilityForDisplay(stack);
        }
    }

    public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate){
        if(stack.hasTagCompound()){
            NBTTagCompound compound = stack.getTagCompound();

            int energy = compound.getInteger("Energy");
            int energyExtracted = Math.min(energy, Math.min(MAX_EXTRACT, maxExtract));

            if(!simulate){
                energy -= energyExtracted;
                compound.setInteger("Energy", energy);
            }

            return energyExtracted;
        }
        return 0;
    }

    public int getEnergyStored(ItemStack stack){
        if(stack.hasTagCompound()){
            return stack.getTagCompound().getInteger("Energy");
        }
        return 0;
    }

    public int getMaxEnergyStored(ItemStack stack){
        return CAPACITY;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound compound){
        return new CapProvider(stack, CAPACITY, MAX_RECEIVE, MAX_EXTRACT);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems){
        super.getSubItems(tab, subItems);

        if(this.isInCreativeTab(tab)){
            ItemStack stack = new ItemStack(this);
            Helper.setItemEnergy(stack, this.getMaxEnergyStored(stack));
            subItems.add(stack);
        }
    }

    public static class CapProvider implements ICapabilityProvider{

        private ItemStack stack;
        private int cap, maxRec, maxTra;

        public CapProvider(ItemStack stack, int cap, int maxRec, int maxTra){
            this.stack = stack;
            this.cap = cap;
            this.maxRec = maxRec;
            this.maxTra = maxTra;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing){
            return getCapability(capability, facing) != null;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing){
            EStorage storage = new EStorage(stack, cap, maxRec, maxTra);
            if(capability == CapabilityEnergy.ENERGY){
                return (T) storage;
            }
            return null;
        }
    }

    public static class EStorage extends EnergyStorage{

        private ItemStack stack;

        public EStorage(ItemStack stack, int capacity){
            super(capacity);
            this.stack = stack;
        }

        public EStorage(ItemStack stack, int capacity, int maxTransfer){
            super(capacity, maxTransfer);
            this.stack = stack;
        }

        public EStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract){
            super(capacity, maxReceive, maxExtract);
            this.stack = stack;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate){
            if (!canReceive())
                return 0;

            int energyReceived = Math.min(capacity - getEnergyStored(), Math.min(this.maxReceive, maxReceive));
            if (!simulate)
                setEnergy(getEnergyStored() + energyReceived);
            return energyReceived;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate){
            if (!canExtract())
                return 0;

            int energyExtracted = Math.min(getEnergyStored(), Math.min(this.maxExtract, maxExtract));
            if (!simulate)
                setEnergy(getEnergyStored() - energyExtracted);
            return energyExtracted;
        }

        @Override
        public int getEnergyStored(){
            if(stack.hasTagCompound()){
                return stack.getTagCompound().getInteger("Energy");
            }
            else{
                return 0;
            }
        }

        public void setEnergy(int energy){
            if(!stack.hasTagCompound()){
                stack.setTagCompound(new NBTTagCompound());
            }

            stack.getTagCompound().setInteger("Energy", energy);
        }
    }
}
