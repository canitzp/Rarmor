package de.canitzp.api.util;

import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class EnergyUtil {

    public static void balanceEnergy(ItemStack[] stacks, int energy){
        int e = energy / stacks.length;
        for(ItemStack stack : stacks){
            NBTUtil.setInteger(stack, "Energy", e);
        }
    }

    public static void balanceEnergy(ItemStack[] stacks){
        int i = 0;
        for(ItemStack stack : stacks){
            i += NBTUtil.getInteger(stack, "Energy");
        }
        balanceEnergy(stacks, i);
    }

}
