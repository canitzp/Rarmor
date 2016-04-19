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
