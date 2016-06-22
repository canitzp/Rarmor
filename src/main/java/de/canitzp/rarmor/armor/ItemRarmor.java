package de.canitzp.rarmor.armor;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.Registry;
import de.canitzp.rarmor.api.ITabTickable;
import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public class ItemRarmor extends ItemGenericRarmor implements ISpecialArmor, IEnergyContainerItem{

    private int maxEnergy = 250000;
    private Map<ItemStack, List<ITabTickable>> tickMap = new HashMap<>();

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
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack){
        if(!this.tickMap.containsKey(stack)){
            this.tickMap.put(stack, RarmorAPI.getNewTickTabs());
        } else {
            for(List<ITabTickable> tabList : this.tickMap.values()){
                for(ITabTickable tabTickable : tabList){
                    tabTickable.tick(world, player, stack);
                }
            }
        }
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
