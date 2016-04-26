package de.canitzp.rarmor;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.inventory.gui.GuiRFArmor;
import de.canitzp.rarmor.items.ItemChainSaw;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorGeneric;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorHelmet;
import de.canitzp.rarmor.util.MinecraftUtil;
import de.canitzp.rarmor.util.NBTUtil;
import de.canitzp.rarmor.util.SlotUtil;
import de.canitzp.rarmor.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

/**
 * @author canitzp
 */
public class RarmorUtil{

    /**
     * @param world The World of the Player
     * @param x     The X-Coordinate of the Tree
     * @param y     The Y-Coordinate of the Tree
     * @param z     The Z-Coordinate of the Tree
     * @param wood  The Log Block
     * @return Is the Structure a Tree
     * <p>
     * Author: mDiyo, boni-xx
     */
    public static boolean detectTree(World world, int x, int y, int z, Block wood){
        int height = y;
        boolean foundTop = false;
        do{
            height++;
            IBlockState block = world.getBlockState(new BlockPos(x, height, z));
            if (block.getBlock() != wood){
                height--;
                foundTop = true;
            }
        }while (!foundTop);
        int numLeaves = 0;
        if (height - y < 50){
            for (int xPos = x - 1; xPos <= x + 1; xPos++){
                for (int yPos = height - 1; yPos <= height + 1; yPos++){
                    for (int zPos = z - 1; zPos <= z + 1; zPos++){
                        IBlockState leaves = WorldUtil.getBlockState(world, xPos, yPos, zPos);
                        if (leaves != null && leaves.getBlock().isLeaves(leaves, world, new BlockPos(xPos, yPos, zPos)))
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
    public static boolean breakTree(World world, int x, int y, int z, int xStart, int yStart, int zStart, ItemStack stack, Block bID, EntityPlayer player, int rfPerUse){
        for (int xPos = x - 1; xPos <= x + 1; xPos++){
            for (int yPos = y; yPos <= y + 1; yPos++){
                for (int zPos = z - 1; zPos <= z + 1; zPos++){
                    Block localBlock = WorldUtil.getBlock(world, xPos, yPos, zPos);
                    if (bID == localBlock){
                        IBlockState localMeta = world.getBlockState(new BlockPos(xPos, yPos, zPos));
                        float localHardness = localBlock.getBlockHardness(localMeta, world, new BlockPos(xPos, yPos, zPos));
                        if (!(localHardness < 0)){
                            boolean cancelHarvest;
                            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, new BlockPos(x, y, z), localMeta, player);
                            MinecraftForge.EVENT_BUS.post(event);
                            cancelHarvest = event.isCanceled();
                            int xDist = xPos - xStart;
                            int yDist = yPos - yStart;
                            int zDist = zPos - zStart;
                            if (9 * xDist * xDist + yDist * yDist + 9 * zDist * zDist < 2500){
                                if (cancelHarvest){
                                    breakTree(world, xPos, yPos, zPos, xStart, yStart, zStart, stack, bID, player, rfPerUse);
                                }else{
                                    if (stack.getItem() instanceof ItemChainSaw && ((ItemChainSaw) stack.getItem()).getEnergyStored(stack) >= rfPerUse){
                                        playerHarvestBlock(world, new BlockPos(xPos, yPos, zPos), player, stack);
                                        ((ItemChainSaw) stack.getItem()).extractEnergy(stack, rfPerUse, false);
                                    }else{
                                        playerHarvestBlock(world, new BlockPos(x, y, z), player, stack);
                                        return false;
                                    }
                                    if (!world.isRemote)
                                        breakTree(world, xPos, yPos, zPos, xStart, yStart, zStart, stack, bID, player, rfPerUse);
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
     * @param pos    The Block position
     * @param player The Player
     * @return If the Block could be harvested normally (so that it drops an item)
     * <p>
     * Author: Ellpeck
     */
    public static boolean playerHarvestBlock(World world, BlockPos pos, EntityPlayer player, ItemStack stack){
        Block block = world.getBlockState(pos).getBlock();
        IBlockState state = world.getBlockState(pos);
        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        TileEntity tile = world.getTileEntity(pos);
        boolean canHarvest = block.canHarvestBlock(world, pos, player);
        if (player instanceof EntityPlayerMP){
            int event = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(), (EntityPlayerMP) player, pos);
            if (event == -1){
                return false;
            }
        }
        if (!world.isRemote){
            block.onBlockHarvested(world, pos, state, player);
        }else{
            world.playAuxSFX(2001, pos, Block.getIdFromBlock(block) + (meta << 12));
        }
        boolean removed = block.removedByPlayer(state, world, pos, player, canHarvest);
        if (removed){
            block.onBlockDestroyedByPlayer(world, pos, state);
            if (!world.isRemote && !player.capabilities.isCreativeMode){
                if (canHarvest){
                    block.harvestBlock(world, player, pos, state, tile, player.getHeldItemMainhand());
                }
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack) != 0){
                    block.dropXpOnBlockBreak(world, pos, block.getExpDrop(state, world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)));
                }

            }
        }
        if (!world.isRemote){
            if (player instanceof EntityPlayerMP){
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new SPacketBlockChange(world, pos));
            }
        }else{
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, Minecraft.getMinecraft().objectMouseOver.sideHit));
        }
        return removed;
    }

    public static boolean isPlayerWearingRarmor(EntityPlayer player){
        if (player != null){
            ItemStack head = player.inventory.armorInventory[3];
            ItemStack body = player.inventory.armorInventory[2];
            ItemStack leggins = player.inventory.armorInventory[1];
            ItemStack boots = player.inventory.armorInventory[0];
            if (head != null && body != null && leggins != null && boots != null){
                if (head.getItem() instanceof ItemRFArmorHelmet && body.getItem() instanceof ItemRFArmorBody && leggins.getItem() instanceof ItemRFArmorGeneric && boots.getItem() instanceof ItemRFArmorGeneric){
                    return true;
                }
            }
        }
        return false;
    }

    public static IRarmorModule getRarmorModule(EntityPlayer player){
        if (isPlayerWearingRarmor(player)){
            ItemStack module = NBTUtil.readSlots(getPlayersRarmorChestplate(player), ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
            if (module != null){
                if (module.getItem() instanceof IRarmorModule){
                    return (IRarmorModule) module.getItem();
                }
            }
        }
        return null;
    }

    public static ItemStack getPlayersRarmorChestplate(EntityPlayer player){
        if (isPlayerWearingRarmor(player)){
            return player.inventory.armorInventory[2];
        }
        return null;
    }

    public static void dropSlot(ItemStack stack, EntityPlayer player){
        if (stack != null){
            if (!player.worldObj.isRemote){
                player.dropItem(stack, false);
            }
        }
    }

    public static Slot toggleSlotInGui(int slotX, int slotY, boolean value){
        if (MinecraftUtil.getMinecraftSide().isClient()){
            GuiScreen gui = MinecraftUtil.getCurrentScreen();
            if (gui instanceof GuiRFArmor){
                Slot slot = SlotUtil.getSlotAtPosition((GuiContainer) gui, slotX, slotY);
                if (slot != null){
                    if (!value){
                        if (((GuiRFArmor) gui).deactivatedSlots.contains(slot)){
                            ((GuiRFArmor) gui).deactivatedSlots.remove(slot);
                        }
                    }else{
                        if (!((GuiRFArmor) gui).deactivatedSlots.contains(slot)){
                            ((GuiRFArmor) gui).deactivatedSlots.add(slot);
                        }
                    }
                }
                return slot;
            }
        }
        return null;
    }

    public static void saveRarmor(EntityPlayer player, InventoryBase base){
        saveRarmor(getPlayersRarmorChestplate(player), base);
    }

    public static void saveRarmor(ItemStack armor, InventoryBase base){
       NBTUtil.saveSlots(armor, base);
    }

    public static InventoryBase readRarmor(EntityPlayer player){
        return readRarmor(getPlayersRarmorChestplate(player));
    }

    public static InventoryBase readRarmor(ItemStack armor){
        return NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount);
    }

}
