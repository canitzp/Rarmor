package de.canitzp.rarmor.items;

import cofh.api.energy.ItemEnergyContainer;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.Util;
import de.canitzp.rarmor.inventory.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author canitzp
 * @alternativeAuthor Ellpeck
 */
public class ItemChainSaw extends ItemEnergyContainer {

    public int rfPerUse;

    public ItemChainSaw(int maxEnergy, int transfer, int rfPerUse, String name){
        super(maxEnergy, transfer);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.rfPerUse = rfPerUse;
        this.setUnlocalizedName(Rarmor.MODID + "." + name);
        this.setTextureName(Rarmor.MODID + ":" + name);
        GameRegistry.registerItem(this, name);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
        return stack;
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
        map.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "ChainSaw Modifier", this.getEnergyStored(stack) >= rfPerUse ? 8.0F : 0.0F, 0));
        return map;
    }

    @Override
    public boolean onBlockStartBreak (ItemStack stack, int x, int y, int z, EntityPlayer player) {
        World world = player.worldObj;
        final Block wood = world.getBlock(x, y, z);
        if(!player.isSneaking() && !(player.worldObj.getBlock(x, y, z) == null) && (wood.isWood(world, x, y, z) || wood.isLeaves(world, x, y, z) || wood == Blocks.brown_mushroom || wood == Blocks.red_mushroom_block) && Util.detectTree(world, x,y,z, wood) && this.getEnergyStored(stack) >= rfPerUse){
            int meta = world.getBlockMetadata(x, y, z);
            boolean b = Util.breakTree(world, x, y, z, x, y, z, stack, stack.stackTagCompound, wood, meta, player, rfPerUse);
            return b || super.onBlockStartBreak(stack, x, y, z, player);
        }
        return super.onBlockStartBreak(stack, x, y, z, player);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack){
        return true;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta){
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
        return true;
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
