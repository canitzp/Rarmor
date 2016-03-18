package de.canitzp.rarmor.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by canitzp.
 */
public class WorldUtil {

    public static Block getBlock(World world, int x, int y, int z){
        return world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static Block getBlock(World world, BlockPos pos){
        return world.getBlockState(pos).getBlock();
    }

    public static IBlockState getBlockState(World world, BlockPos pos){
        return world.getBlockState(pos);
    }

    public static IBlockState getBlockState(World world, int x, int y, int z){
        return world.getBlockState(new BlockPos(x, y, z));
    }

}
