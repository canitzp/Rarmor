/*
 * This file ("ItemRarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.item;

import cofh.api.energy.IEnergyContainerItem;
import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.compat.Compat;
import de.ellpeck.rarmor.mod.compat.ItemTeslaWrapper;
import de.ellpeck.rarmor.mod.misc.CreativeTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;
import java.util.Locale;

public class ItemRarmor extends ItemArmor implements IEnergyContainerItem{

    private static final ArmorMaterial RARMOR_MATERIAL = EnumHelper.addArmorMaterial(RarmorAPI.MOD_ID.toUpperCase(Locale.ROOT)+"_MATERIAL", RarmorAPI.MOD_ID+":rarmorArmor", 0, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);

    private static final int CAPACITY = 300000;
    private static final int MAX_RECEIVE = 1000;
    private static final int MAX_EXTRACT = 1000;

    public ItemRarmor(String name, EntityEquipmentSlot slot){
        super(RARMOR_MATERIAL, -1, slot);

        this.setRegistryName(RarmorAPI.MOD_ID, name);
        GameRegistry.register(this);

        this.setUnlocalizedName(RarmorAPI.MOD_ID+"."+name);
        this.setCreativeTab(CreativeTab.INSTANCE);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
        if(this.isChestplate()){
            if(entity instanceof EntityPlayer){
                IRarmorData data = RarmorAPI.methodHandler.getDataForStack(world, stack, !world.isRemote);
                if(data != null){
                    if(!world.isRemote){
                        data.sendQueuedUpdate((EntityPlayer)entity);
                    }

                    data.tick(world);
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced){
        if(this.isChestplate()){
            IRarmorData data = RarmorAPI.methodHandler.getDataForStack(player.worldObj, stack, false);
            if(data != null){
                String s = "   ";
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

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.RARE;
    }

    private boolean isChestplate(){
        return this.armorType == EntityEquipmentSlot.CHEST;
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

    public void setEnergy(ItemStack stack, int energy){
        if(!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("Energy", energy);
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
}
