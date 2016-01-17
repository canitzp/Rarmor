package de.canitzp.rarmor.items.rfarmor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemRFArmorGeneric extends ItemRFArmor {

    public ItemRFArmorGeneric(ArmorType type, int maxEnergy, int maxTransfer, String name) {
        super(RFARMOR, type, maxEnergy, maxTransfer, name);
        setHasSubtypes(true);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player){
        this.setEnergy(stack, 0);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        double energyDif = getMaxEnergyStored(stack)-getEnergyStored(stack);
        double maxAmount = getMaxEnergyStored(stack);
        return energyDif/maxAmount;
    }

}
