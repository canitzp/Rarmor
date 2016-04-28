/*
 * This file 'PlayerUtil.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class PlayerUtil{


    public static ItemStack getArmor(EntityPlayer player, EntityEquipmentSlot slot){
        return player.inventory.armorInventory[slot.getIndex()];
    }

    public static ItemStack getItemStackFromSlot(EntityPlayer player, EntityEquipmentSlot slot){
        return player.getItemStackFromSlot(slot);
    }

}
