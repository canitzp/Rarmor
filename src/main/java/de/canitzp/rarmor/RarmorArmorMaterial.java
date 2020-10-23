package de.canitzp.rarmor;

import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class RarmorArmorMaterial implements IArmorMaterial {
    
    public static final RarmorArmorMaterial INSTANCE = new RarmorArmorMaterial();
    
    private static final String NAME = String.format("%s_rarmor_armor_material", RarmorAPI.MOD_ID);
    private static final int[] DURABILITY = new int[]{0, 0, 0, 0};
    private static final int[] DAMAGE_REDUCTION = new int[]{2, 5, 6, 2};
    private static final int ENCHANTABILITY = 0;
    public static final int TOUGHNESS = 0;
    
    private RarmorArmorMaterial(){}
    
    @Override
    public int getDurability(EquipmentSlotType slotIn){
        return DURABILITY[slotIn.getIndex()];
    }
    
    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn){
        return DAMAGE_REDUCTION[slotIn.getIndex()];
    }
    
    @Override
    public int getEnchantability(){
        return ENCHANTABILITY;
    }
    
    @Override
    public SoundEvent getSoundEvent(){
        return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
    }
    
    @Override
    public Ingredient getRepairMaterial(){
        return null;
    }
    
    @Override
    public String getName(){
        return NAME;
    }
    
    @Override
    public float getToughness(){
        return TOUGHNESS;
    }
    
    @Override
    public float getKnockbackResistance(){
        return 0.1F; // same as netherite
    }
}
