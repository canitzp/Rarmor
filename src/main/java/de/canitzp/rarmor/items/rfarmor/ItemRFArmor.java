package de.canitzp.rarmor.items.rfarmor;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.registry.GameRegistry;
import de.canitzp.api.util.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;

import java.util.List;

/**
 * @author canitzp
 */
public class ItemRFArmor extends ItemArmor implements IEnergyContainerItem, ISpecialArmor {

    public static final ItemArmor.ArmorMaterial RFARMOR = EnumHelper.addArmorMaterial(Rarmor.MODID + ":RFARMOR", 100, new int[] { 3, 8, 6, 3 }, 0);

    public int maxEnergy;
    public int maxTransfer;
    public double absorbRatio = 0.9D;
    public int energyPerDamage = 160;

    public String[] textures = new String[2];

    public ItemRFArmor(ArmorMaterial material, ArmorType type, int maxEnergy, int maxTransfer, String name) {
        super(material, 0, type.getId());
        setEnergyParams(maxEnergy, maxTransfer);
        setUnlocalizedName(Rarmor.MODID + "." + name);
        setTextureName(Rarmor.MODID + ":" + name);
        setMaxDamage(maxEnergy);
        GameRegistry.registerItem(this, name);
    }

    @Override
    public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type){
        return Rarmor.MODID + ":textures/models/armor/rfarmorLayer" + (this.armorType == 2 ? "2" : "1") + ".png";
    }

    public ItemRFArmor setEnergyParams(int maxEnergy, int maxTransfer) {
        this.maxEnergy = maxEnergy;
        this.maxTransfer = maxTransfer;
        return this;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemToRepair, ItemStack stack) {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean check) {
        list.add(this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack)+" RF");
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.maxEnergy;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return true;
    }

    @Override
    public int getMetadata(int damage){
        return damage;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack stackFull = new ItemStack(this);
        this.setEnergy(stackFull, this.getMaxEnergyStored(stackFull));
        list.add(stackFull);

        ItemStack stackEmpty = new ItemStack(this);
        this.setEnergy(stackEmpty, 0);
        list.add(stackEmpty);
    }

    public void setEnergy(ItemStack stack, int energy){
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null){
            compound = new NBTTagCompound();
        }
        compound.setInteger("Energy", energy);
        stack.setTagCompound(compound);
    }

    protected int getBaseAbsorption() {
        return 20;
    }

    /**
     * Returns a % that each piece absorbs, set sums to 100.
     */
    protected int getAbsorptionRatio() {

        switch (armorType) {
            case 0:
                return 15;
            case 1:
                return 40;
            case 2:
                return 30;
            case 3:
                return 15;
        }
        return 0;
    }

    protected int getEnergyPerDamage(ItemStack stack) {
        return energyPerDamage*2;
    }

    /* ISpecialArmor */
    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        int absorbMax = getEnergyPerDamage(armor) > 0 ? 25 * getEnergyStored(armor) / getEnergyPerDamage(armor) : 0;
        return new ArmorProperties(0, absorbRatio * getArmorMaterial().getDamageReductionAmount(armorType) * 0.025, absorbMax);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        if (getEnergyStored(armor) >= getEnergyPerDamage(armor)) {
            return Math.min(getBaseAbsorption(), 20) * getAbsorptionRatio() / 100;
        }
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack armor, DamageSource source, int damage, int slot) {
        if (armor.getItem() instanceof ItemRFArmorBody && entity instanceof EntityPlayer) {
            if (NBTUtil.getInteger(armor, "BurnTime") > 0 || NBTUtil.getInteger(armor, "GenBurnTime") > 0) {
                if (((EntityPlayer) entity).getEntityWorld().rand.nextInt(10) == 1) {
                    entity.setFire(4);
                }
            }
        }
        extractEnergy(armor, damage * getEnergyPerDamage(armor), false);
    }

    /* IEnergyContainerItem */
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (container.stackTagCompound == null) {
            container.stackTagCompound = new NBTTagCompound();
        }
        int energy = container.stackTagCompound.getInteger("Energy");
        int energyReceived = Math.min(maxEnergy - energy, Math.min(this.maxTransfer, maxReceive));

        if (!simulate) {
            energy += energyReceived;
            container.stackTagCompound.setInteger("Energy", energy);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
            return 0;
        }
        int energy = container.stackTagCompound.getInteger("Energy");
        int energyExtracted = Math.min(energy, Math.min(this.maxTransfer, maxExtract));

        if (!simulate) {
            energy -= energyExtracted;
            container.stackTagCompound.setInteger("Energy", energy);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
            return 0;
        }
        return container.stackTagCompound.getInteger("Energy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return maxEnergy;
    }


    @Override
    public boolean showDurabilityBar(ItemStack stack){
        return stack.stackTagCompound == null || !stack.stackTagCompound.getBoolean("CreativeTab");
    }

    public enum ArmorType{
        HEAD(0),
        BODY(1),
        LEGGINS(2),
        SHOES(3);

        private int id;
        ArmorType(int id){
            this.id=id;
        }

        public int getId() {
            return id;
        }
    }
}
