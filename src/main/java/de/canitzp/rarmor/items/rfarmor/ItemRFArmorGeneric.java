/*
 * This file 'ItemRFArmorGeneric.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items.rfarmor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class ItemRFArmorGeneric extends ItemRFArmor{

    public ItemRFArmorGeneric(EntityEquipmentSlot type, int maxEnergy, int maxTransfer, String name){
        super(RFARMOR, type, maxEnergy, maxTransfer, name);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        double energyDif = getMaxEnergyStored(stack) - getEnergyStored(stack);
        double maxAmount = getMaxEnergyStored(stack);
        return energyDif / maxAmount;
    }

}
