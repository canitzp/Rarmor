/*
 * This file ("ItemRarmorChest.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.compat.Compat;
import de.canitzp.rarmor.compat.ItemTeslaWrapper;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public class ItemRarmorChest extends ItemRarmor implements IEnergyContainerItem{

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

            boolean hat = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EntityEquipmentSlot.HEAD) != null;
            boolean chest = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EntityEquipmentSlot.CHEST) != null;
            boolean pants = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EntityEquipmentSlot.LEGS) != null;
            boolean shoes = RarmorAPI.methodHandler.getHasRarmorInSlot(entity, EntityEquipmentSlot.FEET) != null;
            data.tick(world, entity, hat, chest, pants, shoes);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced){
        String s = "   ";

        tooltip.add(TextFormatting.GOLD+I18n.format(RarmorAPI.MOD_ID+".storedEnergy")+":");
        tooltip.add(TextFormatting.YELLOW+s+this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack));

        IRarmorData data = RarmorAPI.methodHandler.getDataForStack(player.getEntityWorld(), stack, false);
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

    @Override
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

    @Override
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

    @Override
    public int getEnergyStored(ItemStack stack){
        if(stack.hasTagCompound()){
            return stack.getTagCompound().getInteger("Energy");
        }
        return 0;
    }

    @Override
    public int getMaxEnergyStored(ItemStack stack){
        return CAPACITY;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound compound){
        return Compat.teslaLoaded ? new ItemTeslaWrapper(stack, this) : super.initCapabilities(stack, compound);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems){
        super.getSubItems(item, tab, subItems);

        ItemStack stack = new ItemStack(item);
        Helper.setItemEnergy(stack, this.getMaxEnergyStored(stack));
        subItems.add(stack);
    }
}
