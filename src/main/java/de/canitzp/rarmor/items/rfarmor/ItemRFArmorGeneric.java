package de.canitzp.rarmor.items.rfarmor;

import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class ItemRFArmorGeneric extends ItemRFArmor {

    public ItemRFArmorGeneric(ArmorType type, int maxEnergy, int maxTransfer, String name) {
        super(RFARMOR, type, maxEnergy, maxTransfer, name);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        double energyDif = getMaxEnergyStored(stack)-getEnergyStored(stack);
        double maxAmount = getMaxEnergyStored(stack);
        return energyDif/maxAmount;
    }

}
