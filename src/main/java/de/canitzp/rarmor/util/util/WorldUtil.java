package de.canitzp.rarmor.util.util;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
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

}
