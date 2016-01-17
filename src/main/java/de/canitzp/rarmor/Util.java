package de.canitzp.rarmor;

import de.canitzp.rarmor.items.ItemChainSaw;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

/**
 * @author canitzp
 */
public class Util {

    /**
     *
     * @param world The World of the Player
     * @param x     The X-Coordinate of the Tree
     * @param y     The Y-Coordinate of the Tree
     * @param z     The Z-Coordinate of the Tree
     * @param wood  The Log Block
     * @return Is the Structure a Tree
     *
     * Author: mDiyo, boni-xx
     */
    public static boolean detectTree(World world, int x, int y, int z, Block wood) {
        int height = y;
        boolean foundTop = false;
        do{
            height++;
            Block block = world.getBlockState(new BlockPos(x, height, z)).getBlock();
            if (block != wood) {
                height--;
                foundTop = true;
            }
        } while (!foundTop);
        int numLeaves = 0;
        if (height - y < 50) {
            for (int xPos = x - 1; xPos <= x + 1; xPos++) {
                for (int yPos = height - 1; yPos <= height + 1; yPos++) {
                    for (int zPos = z - 1; zPos <= z + 1; zPos++) {
                        Block leaves = world.getBlockState(new BlockPos(xPos, yPos, zPos)).getBlock();
                        if (leaves != null && leaves.isLeaves(world, new BlockPos(xPos, yPos, zPos)))
                            numLeaves++;
                    }
                }
            }
        }

        return numLeaves > 3;
    }

    /**
     * Author: mDiyo, boni-xx
     */
    public static boolean breakTree(World world, int x, int y, int z, int xStart, int yStart, int zStart, ItemStack stack, Block bID, EntityPlayer player, int rfPerUse) {
        for (int xPos = x - 1; xPos <= x + 1; xPos++) {
            for (int yPos = y; yPos <= y + 1; yPos++) {
                for (int zPos = z - 1; zPos <= z + 1; zPos++) {
                    Block localBlock = world.getBlockState(new BlockPos(xPos, yPos, zPos)).getBlock();
                    if (bID == localBlock) {
                        IBlockState localMeta = world.getBlockState(new BlockPos(xPos, yPos, zPos));
                        float localHardness = localBlock.getBlockHardness(world, new BlockPos(xPos, yPos, zPos));
                        if (!(localHardness < 0)) {
                            boolean cancelHarvest;
                            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, new BlockPos(x, y, z), localMeta, player);
                            MinecraftForge.EVENT_BUS.post(event);
                            cancelHarvest = event.isCanceled();
                            int xDist = xPos - xStart;
                            int yDist = yPos - yStart;
                            int zDist = zPos - zStart;
                            if (9 * xDist * xDist + yDist * yDist + 9 * zDist * zDist < 2500) {
                                if (cancelHarvest) {
                                    breakTree(world, xPos, yPos, zPos, xStart, yStart, zStart, stack, bID, player, rfPerUse);
                                } else {
                                    //if (localMeta % 4 == meta % 4) {
                                        if(stack.getItem() instanceof ItemChainSaw && ((ItemChainSaw) stack.getItem()).getEnergyStored(stack) >= rfPerUse){
                                            playerHarvestBlock(world, new BlockPos(xPos, yPos, zPos), player);
                                            ((ItemChainSaw) stack.getItem()).extractEnergy(stack, rfPerUse, false);
                                        } else{
                                            playerHarvestBlock(world, new BlockPos(x, y, z), player);
                                            return false;
                                        }
                                        if (!world.isRemote)
                                            breakTree(world, xPos, yPos, zPos, xStart, yStart, zStart, stack, bID, player, rfPerUse);
                                    //}
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Harvests a Block by a Player
     *
     * @param world  The World
     * @param pos  The Block position
     * @param player The Player
     * @return If the Block could be harvested normally (so that it drops an item)
     *
     * Author: Ellpeck
     */
    public static boolean playerHarvestBlock(World world, BlockPos pos, EntityPlayer player){
        Block block = world.getBlockState(pos).getBlock();
        IBlockState state = world.getBlockState(pos);
        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        TileEntity tile = world.getTileEntity(pos);
        boolean canHarvest = block.canHarvestBlock(world, pos, player);
        if(player instanceof EntityPlayerMP){
            int event = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP)player).theItemInWorldManager.getGameType(), (EntityPlayerMP)player, pos);
            if(event == -1){
                return false;
            }
        }
        if(!world.isRemote){
            block.onBlockHarvested(world, pos, state, player);
        }
        else{
            world.playAuxSFX(2001, pos, Block.getIdFromBlock(block)+(meta << 12));
        }
        boolean removed = block.removedByPlayer(world, pos, player, canHarvest);
        if(removed){
            block.onBlockDestroyedByPlayer(world, pos, state);
            if(!world.isRemote && !player.capabilities.isCreativeMode){
                if(canHarvest){
                    block.harvestBlock(world, player, pos, state, tile);
                }
                if(!EnchantmentHelper.getSilkTouchModifier(player)){
                    block.dropXpOnBlockBreak(world, pos, block.getExpDrop(world, pos, EnchantmentHelper.getFortuneModifier(player)));
                }
            }
        }
        if(!world.isRemote){
            if(player instanceof EntityPlayerMP){
                ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(world, pos));
            }
        }
        else{
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, Minecraft.getMinecraft().objectMouseOver.sideHit));
        }
        return removed;
    }


}
