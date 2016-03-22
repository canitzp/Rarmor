package de.canitzp.rarmor.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * @author canitzp
 */
public class PlayerUtil {

    public static ItemStack getArmor(EntityEquipmentSlot slot){
        if(MinecraftUtil.getMinecraftSide().isClient()){
            return MinecraftUtil.getPlayer().inventory.armorInventory[slot.getIndex()];
        }
        return new ItemStack(Blocks.fire);
    }

    public static ItemStack getArmor(EntityPlayer player, EntityEquipmentSlot slot){
        return player.inventory.armorInventory[slot.getIndex()];
    }

    public static ItemStack getHoldItemInMainHand(EntityPlayer player){
        return player.getHeldItem(EnumHand.MAIN_HAND);
    }

    public static ItemStack getHoldItemInOffHand(EntityPlayer player){
        return player.getHeldItem(EnumHand.OFF_HAND);
    }

    public static ItemStack getItemStackFromSlot(EntityPlayer player, EntityEquipmentSlot slot){
        return player.getItemStackFromSlot(slot);
    }

}
