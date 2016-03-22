package de.canitzp.rarmor.items;

import cofh.api.energy.ItemEnergyContainer;
import com.google.common.collect.Multimap;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.util.EnergyUtil;
import de.canitzp.rarmor.util.WorldUtil;
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
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
        this.setHarvestLevel("axe", 3);
        Rarmor.registerItem(this, name);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
        int use = rfPerUse;
        if(this.getEnergyStored(stack) >= use){
            this.extractEnergy(stack, use, false);
        }
        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack){
        Multimap<String, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
        if(slot == EntityEquipmentSlot.MAINHAND){
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "ChainSaw Modifier", this.getEnergyStored(stack) >= rfPerUse ? 8.0F : 0.5F, 0));
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool Modifier", -2.5F, 0));
        }
        return map;
    }

    @Override
    public boolean onBlockStartBreak (ItemStack stack, BlockPos pos, EntityPlayer player) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        World world = player.worldObj;
        final Block wood = WorldUtil.getBlock(world, pos);
        if(!player.isSneaking() && !(world.getBlockState(pos).getBlock() == null) && (wood.isWood(world, pos) || wood.isLeaves(world.getBlockState(pos), world, pos) || wood == Blocks.brown_mushroom || wood == Blocks.red_mushroom_block) && RarmorUtil.detectTree(world, pos.getX(), pos.getY(), pos.getZ(), wood) && this.getEnergyStored(stack) >= rfPerUse){
            boolean b = RarmorUtil.breakTree(world, x, y, z, x, y, z, stack, wood, player, rfPerUse);
            return b || super.onBlockStartBreak(stack, pos, player);
        }
        return super.onBlockStartBreak(stack, pos, player);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state){
        return true;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state){
        Block block = state.getBlock();
        return this.getEnergyStored(stack) >= rfPerUse ? ((block instanceof BlockLog || block.getMaterial(state) == Material.wood || block.getMaterial(state) == Material.leaves)? 5F : 1.0F) : 1.0F;
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
        EnergyUtil.setEnergy(stack, 0);
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
        EnergyUtil.setEnergy(stackFull, this.getMaxEnergyStored(stackFull));
        list.add(stackFull);

        ItemStack stackEmpty = new ItemStack(this);
        EnergyUtil.setEnergy(stackEmpty, 0);
        list.add(stackEmpty);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool){
        list.add(this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack)+" RF");
    }

}
