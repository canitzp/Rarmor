package de.canitzp.rarmor.api;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public interface IToolModule {

    String getName();

    default void onHitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){}

    default void onBlockDestroyed(ItemStack stack, World world, Block block, BlockPos pos, EntityLivingBase player){}

    default ItemStack onRightClick(ItemStack stack, World world, EntityPlayer player){return stack;}

    default boolean canHarvestBlock(Block block)
    {
        return true;
    }

}
