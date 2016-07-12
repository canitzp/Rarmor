package de.canitzp.rarmor.armor;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.RarmorValues;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import java.util.List;

/**
 * @author canitzp
 */
public class ItemRarmor extends ItemGenericRarmor implements ISpecialArmor, IEnergyContainerItem{

    public ItemRarmor(EntityEquipmentSlot equipmentSlotIn){
        super(equipmentSlotIn);
        this.setMaxDamage(Rarmor.rarmorMaterial.getDurability(equipmentSlotIn));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add(NBTUtil.getEnergy(stack) + "/" + RarmorValues.rarmorMaxEnergy + " RF");
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack){
        if(!RarmorAPI.tickMap.containsKey(stack)){
            RarmorAPI.tickMap.put(stack, RarmorAPI.createNewTabs());
            if(!world.isRemote){
                for(IRarmorTab tabTickable : RarmorAPI.tickMap.get(stack)){
                    tabTickable.readFromNBT(NBTUtil.getTagFromStack(stack).getCompoundTag(tabTickable.getTabIdentifier(RarmorUtil.getRarmorChestplate(player), player)));
                }
                ((EntityPlayerMP)player).connection.sendPacket(new SPacketSetSlot(-2, 38, stack));
            }
        }
        for(IRarmorTab tabTickable : RarmorAPI.tickMap.get(stack)){
            tabTickable.tick(world, player, stack);
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
        return RarmorAPI.receiveEnergy(container, maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate){
        return RarmorAPI.extractEnergy(container, maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ItemStack container){
        return NBTUtil.getEnergy(container);
    }

    @Override
    public int getMaxEnergyStored(ItemStack container){
        return RarmorValues.rarmorMaxEnergy;
    }
}
