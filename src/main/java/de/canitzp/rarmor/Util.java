package de.canitzp.rarmor;

import de.canitzp.rarmor.items.ItemChainSaw;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.World;
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
            Block block = world.getBlock(x, height, z);
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
                        Block leaves = world.getBlock(xPos, yPos, zPos);
                        if (leaves != null && leaves.isLeaves(world, xPos, yPos, zPos))
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
    public static boolean breakTree(World world, int x, int y, int z, int xStart, int yStart, int zStart, ItemStack stack, NBTTagCompound tags, Block bID, int meta, EntityPlayer player, int rfPerUse) {
        for (int xPos = x - 1; xPos <= x + 1; xPos++) {
            for (int yPos = y; yPos <= y + 1; yPos++) {
                for (int zPos = z - 1; zPos <= z + 1; zPos++) {
                    Block localBlock = world.getBlock(xPos, yPos, zPos);
                    if (bID == localBlock) {
                        int localMeta = world.getBlockMetadata(xPos, yPos, zPos);
                        float localHardness = localBlock.getBlockHardness(world, xPos, yPos, zPos);
                        if (!(localHardness < 0)) {
                            boolean cancelHarvest;
                            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(x, y, z, world, localBlock, localMeta, player);
                            MinecraftForge.EVENT_BUS.post(event);
                            cancelHarvest = event.isCanceled();
                            int xDist = xPos - xStart;
                            int yDist = yPos - yStart;
                            int zDist = zPos - zStart;
                            if (9 * xDist * xDist + yDist * yDist + 9 * zDist * zDist < 2500) {
                                if (cancelHarvest) {
                                    breakTree(world, xPos, yPos, zPos, xStart, yStart, zStart, stack, tags, bID, meta, player, rfPerUse);
                                } else {
                                    if (localMeta % 4 == meta % 4) {
                                        if(stack.getItem() instanceof ItemChainSaw && ((ItemChainSaw) stack.getItem()).getEnergyStored(stack) >= rfPerUse){
                                            playerHarvestBlock(world, xPos, yPos, zPos, player);
                                            ((ItemChainSaw) stack.getItem()).extractEnergy(stack, rfPerUse, false);
                                        } else{
                                            playerHarvestBlock(world, x, y, z, player);
                                            return false;
                                        }
                                        if (!world.isRemote)
                                            breakTree(world, xPos, yPos, zPos, xStart, yStart, zStart, stack, tags, bID, meta, player, rfPerUse);
                                    }
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
     * @param xPos   The X Coordinate
     * @param yPos   The Y Coordinate
     * @param zPos   The Z Coordinate
     * @param player The Player
     * @return If the Block could be harvested normally (so that it drops an item)
     *
     * Author: Ellpeck
     */
    public static boolean playerHarvestBlock(World world, int xPos, int yPos, int zPos, EntityPlayer player){
        Block block = world.getBlock(xPos, yPos, zPos);
        int meta = world.getBlockMetadata(xPos, yPos, zPos);
        boolean canHarvest = block.canHarvestBlock(player, meta);
        if(!world.isRemote){
            block.onBlockHarvested(world, xPos, yPos, zPos, meta, player);
        }
        else{
            world.playAuxSFX(2001, xPos, yPos, zPos, Block.getIdFromBlock(block)+(meta << 12));
        }
        boolean removed = block.removedByPlayer(world, player, xPos, yPos, zPos, canHarvest);
        if(removed){
            block.onBlockDestroyedByPlayer(world, xPos, yPos, zPos, meta);
            if(!world.isRemote && !player.capabilities.isCreativeMode){
                if(canHarvest){
                    block.harvestBlock(world, player, xPos, yPos, zPos, meta);
                }
                if(!EnchantmentHelper.getSilkTouchModifier(player)){
                    block.dropXpOnBlockBreak(world, xPos, yPos, zPos, block.getExpDrop(world, meta, EnchantmentHelper.getFortuneModifier(player)));
                }
            }
        }
        if(!world.isRemote){
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(xPos, yPos, zPos, world));
        }
        else{
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(2, xPos, yPos, zPos, Minecraft.getMinecraft().objectMouseOver.sideHit));
        }
        return removed;
    }


}
