/*
 * This file 'IToolModule.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.api.modules;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public interface IToolModule{

    String getName();

    default float onHitEntity(ItemStack module, ItemStack tool, EntityLivingBase target, EntityLivingBase attacker){
        return 0.0F;
    }

    default boolean onBlockDestroyed(ItemStack module, ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity){
        return true;
    }

    default ItemStack onRightClick(ItemStack module, ItemStack stack, World world, EntityPlayer player){
        return stack;
    }

    default boolean onBlockStartBreak(ItemStack module, ItemStack stack, IBlockState state, EntityPlayer player, BlockPos pos){
        return false;
    }

    default float getStrengthAgainstBlock(ItemStack module, IBlockState state, ItemStack stack){
        return 1.0F;
    }

    default boolean canHarvestBlock(ItemStack module, IBlockState state, ItemStack stack){
        return false;
    }
}
