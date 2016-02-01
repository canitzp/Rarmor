package de.canitzp.rarmor.blocks;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.inventory.GuiHandler;
import de.canitzp.rarmor.tileEntities.TileEntityCircuitCreator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author canitzp
 */
public class BlockCircuitCreator extends BlockContainer {

    public BlockCircuitCreator() {
        super(Material.iron);
        String name = "blockCircuitCreator";
        setRegistryName(Rarmor.MODID, name);
        setUnlocalizedName(Rarmor.MODID + ":" + name);
        Rarmor.proxy.addRenderer(new ItemStack(this), name);
        GameRegistry.registerBlock(this, name);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote){
            playerIn.openGui(Rarmor.instance, GuiHandler.CIRCUITCREATOR, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCircuitCreator();
    }

}
