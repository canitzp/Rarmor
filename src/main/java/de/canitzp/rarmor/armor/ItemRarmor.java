package de.canitzp.rarmor.armor;

import cofh.api.energy.IEnergyContainerItem;
import com.google.common.base.Predicate;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author canitzp
 */
public class ItemRarmor extends ItemGenericRarmor implements ISpecialArmor, IEnergyContainerItem{

    public ItemRarmor(EntityEquipmentSlot equipmentSlotIn){
        super(equipmentSlotIn);
        this.setMaxDamage(Rarmor.rarmorMaterial.getDurability(equipmentSlotIn));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
        super.addInformation(stack, playerIn, tooltip, advanced);
        List<IRarmorTab> tabs = RarmorAPI.getTabsFromStack(playerIn.getEntityWorld(), stack);
        if(tabs != null){
            for(IRarmorTab tab : tabs){
                if(tab != null && tab.canBeVisible(stack, playerIn)){
                    tab.addInformation(stack, playerIn, tooltip, advanced);
                }
            }
        }
        tooltip.add(NBTUtil.getEnergy(stack) + "/" + RarmorValues.rarmorMaxEnergy + " RF");
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (!RarmorAPI.hasRarmorTabs(world, stack)) {
            RarmorAPI.setNewTabsToStack(player, stack);
            if (!world.isRemote) {
                for (IRarmorTab tab : RarmorAPI.getTabsFromStack(world, stack)) {
                    NBTTagCompound tabNBT = NBTUtil.getTagFromStack(stack).getCompoundTag(tab.getTabIdentifier(stack, player));
                    tab.readFromNBT(tabNBT);
                }
                //((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(-2, 38, stack));
            }
        }
        for (IRarmorTab tab : RarmorAPI.getTabsFromStack(world, stack)) {
            tab.tick(world, player, stack);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
    	return 1d - ((double) NBTUtil.getEnergy(stack) / (double) RarmorValues.rarmorMaxEnergy);
//        return (RarmorValues.rarmorMaxEnergy-NBTUtil.getEnergy(stack))/RarmorValues.rarmorMaxEnergy;
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
        this.extractEnergy(stack, 500, false);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean showDurabilityBar(ItemStack stack){
        return NBTUtil.getEnergy(stack) != RarmorValues.rarmorMaxEnergy;
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
