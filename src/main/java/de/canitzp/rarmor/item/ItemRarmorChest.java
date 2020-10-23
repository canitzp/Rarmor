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
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemRarmorChest extends ItemRarmor{

    public static final int CAPACITY = 300000;
    private static final int MAX_RECEIVE = 1000;
    private static final int MAX_EXTRACT = 1000;

    public ItemRarmorChest(){
        super(EquipmentSlotType.CHEST);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
        IRarmorData data = RarmorAPI.methodHandler.getDataForStack(world, stack, !world.isRemote);
        if(data != null){
            if(!world.isRemote && entity instanceof PlayerEntity){
                data.sendQueuedUpdate((PlayerEntity) entity);
            }

            boolean hat = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EquipmentSlotType.HEAD) != ItemStack.EMPTY;
            boolean chest = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EquipmentSlotType.CHEST) != ItemStack.EMPTY;
            boolean pants = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EquipmentSlotType.LEGS) != ItemStack.EMPTY;
            boolean shoes = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EquipmentSlotType.FEET) != ItemStack.EMPTY;
            data.tick(world, entity, hat, chest, pants, shoes);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag){
        tooltip.add(new StringTextComponent(TextFormatting.GOLD.toString()).append(new TranslationTextComponent(RarmorAPI.MOD_ID + ".stored_energy")).appendString(":"));
        tooltip.add(new StringTextComponent(TextFormatting.YELLOW.toString() + "   " + this.getEnergyStored(stack) + "/" + this.getMaxEnergyStored(stack)));
        if(world != null){
            IRarmorData data = RarmorAPI.methodHandler.getDataForStack(world, stack, false);
            if(data != null){
                tooltip.add(new StringTextComponent(TextFormatting.GOLD.toString()).append(new TranslationTextComponent(RarmorAPI.MOD_ID + ".installed_modules")).appendString(":"));
                for(ActiveRarmorModule module : data.getCurrentModules()){
                    tooltip.add(new StringTextComponent(TextFormatting.YELLOW.toString() + "   -").append(new TranslationTextComponent("module." + module.getIdentifier() + ".name")));
                }
                tooltip.add(new StringTextComponent(TextFormatting.GOLD.toString()).append(new TranslationTextComponent(RarmorAPI.MOD_ID + ".stackId")).appendString(":"));
                tooltip.add(new StringTextComponent(TextFormatting.YELLOW.toString() + "   " + RarmorAPI.methodHandler.checkAndSetRarmorId(stack, false)));
            } else {
                tooltip.add(new StringTextComponent(TextFormatting.RED.toString() + TextFormatting.ITALIC.toString()).append(new TranslationTextComponent(RarmorAPI.MOD_ID + ".noDataYet")));
            }
        }
    }

    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate){
        if(!stack.hasTag()){
            stack.setTag(new CompoundNBT());
        }
        CompoundNBT compound = stack.getTag();

        int energy = compound.getInt("Energy");
        int energyReceived = Math.min(CAPACITY-energy, Math.min(MAX_RECEIVE, maxReceive));

        if(!simulate){
            energy += energyReceived;
            compound.putInt("Energy", energy);
        }

        return energyReceived;
    }

    public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate){
        if(stack.hasTag()){
            CompoundNBT compound = stack.getTag();

            int energy = compound.getInt("Energy");
            int energyExtracted = Math.min(energy, Math.min(MAX_EXTRACT, maxExtract));

            if(!simulate){
                energy -= energyExtracted;
                compound.putInt("Energy", energy);
            }

            return energyExtracted;
        }
        return 0;
    }

    public int getEnergyStored(ItemStack stack){
        if(stack.hasTag()){
            return stack.getTag().getInt("Energy");
        }
        return 0;
    }

    public int getMaxEnergyStored(ItemStack stack){
        return CAPACITY;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT compound){
        return new CapProvider(stack, CAPACITY, MAX_RECEIVE, MAX_EXTRACT);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> subItems){
        super.fillItemGroup(group, subItems);

        if(this.isInGroup(group)){
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
        public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side){
            if(capability == CapabilityEnergy.ENERGY){
                return LazyOptional.of(() -> (T) new EStorage(stack, cap, maxRec, maxTra));
            }
            return LazyOptional.empty();
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
            if(stack.hasTag()){
                return stack.getTag().getInt("Energy");
            }
            else{
                return 0;
            }
        }

        public void setEnergy(int energy){
            if(!stack.hasTag()){
                stack.setTag(new CompoundNBT());
            }

            stack.getTag().putInt("Energy", energy);
        }
    }
}
