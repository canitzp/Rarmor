package de.canitzp.rarmor.armor;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.Registry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

import java.util.List;

/**
 * @author canitzp
 */
public class ItemRarmor extends ItemGenericRarmor implements ISpecialArmor, IEnergyContainerItem{

    private int maxEnergy = 250000;

    public ItemRarmor(EntityEquipmentSlot equipmentSlotIn){
        super(equipmentSlotIn);
        this.setMaxDamage(Rarmor.rarmorMaterial.getDurability(equipmentSlotIn));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add(NBTUtil.getEnergy(stack) + "/" + this.maxEnergy + " RF");
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot){
        return new ArmorProperties(0, 0.025, 10);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot){
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot){
        this.receiveEnergy(stack, 500, false);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
        return false;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack){
        return true;
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate){
        int currentEnergy = NBTUtil.getEnergy(container);
        int energyReceived = Math.min(this.maxEnergy - NBTUtil.getEnergy(container), maxReceive);
        if(!simulate){
            NBTUtil.setEnergy(container, currentEnergy + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate){
        int currentEnergy = NBTUtil.getEnergy(container);
        int energyExtracted = Math.min(currentEnergy, maxExtract);
        if(!simulate){
            NBTUtil.setEnergy(container, currentEnergy - energyExtracted);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored(ItemStack container){
        return NBTUtil.getEnergy(container);
    }

    @Override
    public int getMaxEnergyStored(ItemStack container){
        return this.maxEnergy;
    }
}
