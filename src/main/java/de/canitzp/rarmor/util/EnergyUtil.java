package de.canitzp.rarmor.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static de.canitzp.rarmor.util.PropertyUtil.ENERGY;

/**
 * @author canitzp
 */
public class EnergyUtil {

    public static void balanceEnergy(ItemStack[] stacks, int energy) {
        int e = energy / stacks.length;
        for (ItemStack stack : stacks) {
            NBTUtil.setInteger(stack, "Energy", e);
        }
    }

    public static void balanceEnergy(ItemStack[] stacks) {
        int i = 0;
        for (ItemStack stack : stacks) {
            i += NBTUtil.getInteger(stack, "Energy");
        }
        balanceEnergy(stacks, i);
    }

    public static void addEnergy(ItemStack stack, int energy, int maxEnergy) {
        int currentEnergy = NBTUtil.getInteger(stack, "Energy");
        if (maxEnergy >= (currentEnergy + energy)) {
            currentEnergy += energy;
            NBTUtil.setInteger(stack, "Energy", currentEnergy);
        }
    }

    public static void reduceEnergy(ItemStack stack, int energy) {
        int currentEnergy = NBTUtil.getInteger(stack, "Energy");
        currentEnergy -= energy;
        NBTUtil.setInteger(stack, "Energy", currentEnergy);
    }

    public static int getEnergy(ItemStack stack) {
        return NBTUtil.getInteger(stack, "Energy");
    }

    public static void setEnergy(ItemStack stack, int energy) {
        NBTUtil.setInteger(stack, "Energy", energy);
    }

    public static void setEnergy(World world, BlockPos pos, int energy) {
        world.setBlockState(pos, world.getBlockState(pos).withProperty(ENERGY, energy));
    }

    public static void addEnergy(World world, BlockPos pos, int energy) {
        world.setBlockState(pos, world.getBlockState(pos).withProperty(ENERGY, world.getBlockState(pos).getValue(ENERGY) + energy));
    }

    public static int getEnergy(World world, BlockPos pos) {
        return world.getBlockState(pos).getValue(ENERGY);
    }

    public static int getMaxEnergy() {
        return ENERGY.getAllowedValues().size();
    }

}
