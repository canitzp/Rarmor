package de.canitzp.rarmor.items;

import cofh.api.energy.ItemEnergyContainer;
import com.google.common.collect.Multimap;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.util.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author canitzp
 */
public class ItemChainSaw extends ItemEnergyContainer {

    public int rfPerUse;

    public ItemChainSaw(int maxEnergy, int transfer, int rfPerUse, String name){
        super(maxEnergy, transfer);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.rfPerUse = rfPerUse;
        this.setUnlocalizedName(Rarmor.MODID + "." + name);
        this.setRegistryName(Rarmor.MODID + "." + name);
        this.setCreativeTab(Rarmor.rarmorTab);
        this.setHarvestLevel("axe", 3);
        Rarmor.proxy.addRenderer(new ItemStack(this), name);
        GameRegistry.registerItem(this, name);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2){
        int use = rfPerUse;
        if(this.getEnergyStored(stack) >= use){
            this.extractEnergy(stack, use, false);
        }
        return true;
    }

    @Override
    public Multimap getAttributeModifiers(ItemStack stack){
        Multimap map = super.getAttributeModifiers(stack);
        map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "ChainSaw Modifier", this.getEnergyStored(stack) >= rfPerUse ? 8.0F : 0.0F, 0));
        return map;
    }

    @Override
    public boolean onBlockStartBreak (ItemStack stack, BlockPos pos, EntityPlayer player) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        World world = player.worldObj;
        final Block wood = WorldUtil.getBlock(world, pos);
        if(!player.isSneaking() && !(player.worldObj.getBlockState(pos).getBlock() == null) && (wood.isWood(world, pos) || wood.isLeaves(world, pos) || wood == Blocks.brown_mushroom || wood == Blocks.red_mushroom_block) && RarmorUtil.detectTree(world, pos.getX(), pos.getY(), pos.getZ(), wood) && this.getEnergyStored(stack) >= rfPerUse){
            boolean b = RarmorUtil.breakTree(world, x, y, z, x, y, z, stack, wood, player, rfPerUse);
            return b || super.onBlockStartBreak(stack, pos, player);
        }
        return super.onBlockStartBreak(stack, pos, player);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack){
        return true;
    }

    @Override
    public float getDigSpeed(ItemStack stack, IBlockState state){
        Block block = state.getBlock();
        return this.getEnergyStored(stack) >= rfPerUse ? ((block instanceof BlockLog || block.getMaterial() == Material.wood || block.getMaterial() == Material.leaves)? 5F : 1.0F) : 1.0F;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack){
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.add("axe");
        hashSet.add("sword");
        return hashSet;
    }

    @Override
    public boolean getShareTag(){
        return true;
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player){
        this.setEnergy(stack, 0);
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack){
        return getEnergyStored(itemStack) < getMaxEnergyStored(itemStack);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        double energyDif = getMaxEnergyStored(stack)-getEnergyStored(stack);
        double maxAmount = getMaxEnergyStored(stack);
        return energyDif/maxAmount;
    }

    @Override
    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list){
        ItemStack stackFull = new ItemStack(this);
        this.setEnergy(stackFull, this.getMaxEnergyStored(stackFull));
        list.add(stackFull);

        ItemStack stackEmpty = new ItemStack(this);
        this.setEnergy(stackEmpty, 0);
        list.add(stackEmpty);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool){
        list.add(this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack)+" RF");
    }

    public void setEnergy(ItemStack stack, int energy){
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null){
            compound = new NBTTagCompound();
        }
        compound.setInteger("Energy", energy);
        stack.setTagCompound(compound);
    }



}
