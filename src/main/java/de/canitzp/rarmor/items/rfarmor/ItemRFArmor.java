/*
 * This file 'ItemRFArmor.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items.rfarmor;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.Colors;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.newnetwork.PacketUpdateRarmorData;
import de.canitzp.rarmor.newnetwork.RarmorData;
import de.canitzp.rarmor.newnetwork.WorldData;
import de.canitzp.rarmor.util.EnergyUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;
import sun.nio.ch.Net;

import java.util.List;
import java.util.Random;

/**
 * @author canitzp
 */
@SuppressWarnings("unchecked")
public class ItemRFArmor extends ItemArmor implements IEnergyContainerItem, ISpecialArmor{

    public static final ArmorMaterial RFARMOR = EnumHelper.addEnum(ArmorMaterial.class, Rarmor.MODID + ":RFARMOR", new Class[]{String.class, int.class, int[].class, int.class, SoundEvent.class, float.class}, "", 100, new int[]{4, 9, 7, 4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 2.0F);//EnumHelper.addArmorMaterial(Rarmor.MODID + ":RFARMOR", "", 100, new int[]{4, 9, 7, 4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN);

    public int maxEnergy;
    public int maxTransfer;
    public double absorbRatio = 0.9D;
    public int energyPerDamage = 160;
    public static Colors tabColor = Colors.values()[MathHelper.getRandomIntegerInRange(new Random(), 0, Colors.values().length-1)];

    public String[] textures = new String[2];

    public ItemRFArmor(ArmorMaterial material, EntityEquipmentSlot type, int maxEnergy, int maxTransfer, String name){
        super(material, 0, type);
        setCreativeTab(Rarmor.rarmorTab);
        setEnergyParams(maxEnergy, maxTransfer);
        setHasSubtypes(true);
        Rarmor.registerItem(this, name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World world, EntityPlayer player, EnumHand hand){
        //TODO Remove - test code!!
        if(player.isSneaking()){
            RarmorData data = RarmorData.getDataForRarmor(itemStackIn, world.isRemote);
            if(itemStackIn.hasTagCompound()){
                System.out.println(itemStackIn.getTagCompound().getUniqueId("RarmorID"));
            }
            System.out.println("Before changing vars: "+data);
            if(!world.isRemote){
                System.out.println("Changing vars");
                data.saveAndSyncInt = world.rand.nextInt(100);
                data.onlySaveInt = world.rand.nextInt(100);
                WorldData.makeDirty();
            }
            System.out.println("Before syncing: "+data);
            if(!world.isRemote && player instanceof EntityPlayerMP){
                NetworkHandler.wrapper.sendToAll(data.getSyncMessage());
                System.out.println("SYNCING!");
            }
            System.out.println("After syncing: "+RarmorData.getDataForRarmor(itemStackIn, world.isRemote) + " (If on client, this obviously won't have changed right after the syncing, so right-click the item again to see the changes worked!)");

            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
        }

        else{
            EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemStackIn);
            ItemStack itemstack = player.getItemStackFromSlot(entityequipmentslot);
            if(itemstack == null){
                player.setItemStackToSlot(entityequipmentslot, itemStackIn.copy());
                itemStackIn.stackSize = 0;
                return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
            }
            else{
                return new ActionResult(EnumActionResult.FAIL, itemStackIn);
            }
        }
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player){
        EnergyUtil.setEnergy(stack, 0);
        NBTUtil.setInteger(stack, "color", tabColor.colorValue);
        NBTUtil.setString(stack, "colorName", tabColor.getName());
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
        if((type != null && type.equals("overlay")) || NBTUtil.getBoolean(stack, "isTransparent")){
            return "rarmor:textures/models/armor/rfarmorOverlay.png";
        }
        return Rarmor.MODID + ":textures/models/armor/rfarmorLayer" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
    }

    public ItemRFArmor setEnergyParams(int maxEnergy, int maxTransfer){
        this.maxEnergy = maxEnergy;
        this.maxTransfer = maxTransfer;
        return this;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemToRepair, ItemStack stack){
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean check){
        if(NBTUtil.getString(stack, "colorName") != null){
            list.add(NBTUtil.getString(stack, "colorName"));
        }
        list.add(this.getEnergyStored(stack) + "/" + this.getMaxEnergyStored(stack) + " RF");
    }

    @Override
    public void setDamage(ItemStack stack, int damage){
        EnergyUtil.reduceEnergy(stack, damage);
    }

    @Override
    public int getMaxDamage(ItemStack stack){
        return this.maxEnergy;
    }

    @Override
    public boolean isDamaged(ItemStack stack){
        return !(this.getEnergyStored(stack) == this.getMaxDamage(stack));
    }

    @Override
    public int getMetadata(int damage){
        return damage;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list){
        ItemStack stack = new ItemStack(this);
        NBTUtil.setInteger(stack, "color", tabColor.colorValue);
        NBTUtil.setString(stack, "colorName", tabColor.getName());
        EnergyUtil.setEnergy(stack, this.getMaxEnergyStored(stack));
        list.add(stack);
        ItemStack stack1 = stack.copy();
        EnergyUtil.setEnergy(stack1, 0);
        list.add(stack1);
    }

    protected int getBaseAbsorption(){
        return 20;
    }

    /**
     * Returns a % that each piece absorbs, set sums to 100.
     */
    protected int getAbsorptionRatio(){

        switch(armorType){
            case FEET:
                return 15;
            case LEGS:
                return 40;
            case CHEST:
                return 30;
            case HEAD:
                return 15;
        }
        return 0;
    }

    protected int getEnergyPerDamage(ItemStack stack){
        return energyPerDamage * 2;
    }

    /* ISpecialArmor */
    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot){
        //int absorbMax = getEnergyPerDamage(armor) > 0 ? 25 * getEnergyStored(armor) / getEnergyPerDamage(armor) : 0;
        int absorbMax = getEnergyPerDamage(armor) * getEnergyStored(armor);
        return new ArmorProperties(0, absorbRatio * getArmorMaterial().getDamageReductionAmount(armorType) * 0.025, absorbMax);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot){
        if(getEnergyStored(armor) >= getEnergyPerDamage(armor)){
            return Math.min(getBaseAbsorption(), 20) * getAbsorptionRatio() / 100;
        }
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack armor, DamageSource source, int damage, int slot){
        if(armor.getItem() instanceof ItemRFArmorBody && entity instanceof EntityPlayer){
            if(NBTUtil.getInteger(armor, "BurnTime") > 0 || NBTUtil.getInteger(armor, "GenBurnTime") > 0){
                if(entity.getEntityWorld().rand.nextInt(10) == 1){
                    entity.setFire(4);
                }
            }
        }
        extractEnergy(armor, damage * getEnergyPerDamage(armor), false);
    }

    /* IEnergyContainerItem */
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate){
        if(container.getTagCompound() == null){
            container.setTagCompound(new NBTTagCompound());
        }
        int energy = container.getTagCompound().getInteger("Energy");
        int energyReceived = Math.min(maxEnergy - energy, Math.min(this.maxTransfer, maxReceive));

        if(!simulate){
            energy += energyReceived;
            container.getTagCompound().setInteger("Energy", energy);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate){
        if(container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy")){
            return 0;
        }
        int energy = container.getTagCompound().getInteger("Energy");
        int energyExtracted = Math.min(energy, Math.min(this.maxTransfer, maxExtract));

        if(!simulate){
            energy -= energyExtracted;
            container.getTagCompound().setInteger("Energy", energy);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored(ItemStack container){
        return EnergyUtil.getEnergy(container);
    }

    @Override
    public int getMaxEnergyStored(ItemStack container){
        return maxEnergy;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack){
        return !(NBTUtil.getInteger(stack, "Energy") == this.maxEnergy);
    }

    @Override
    public int getColor(ItemStack stack) {
        return stack.getTagCompound() != null && stack.getTagCompound().hasKey("color") ? NBTUtil.getInteger(stack, "color") : super.getColor(stack);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return EnergyUtil.getEnergy(stack);
    }
}
