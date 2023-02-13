package de.canitzp.rarmor;

import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class RarmorArmorMaterial implements ArmorMaterial {
    
    public static final RarmorArmorMaterial INSTANCE = new RarmorArmorMaterial();
    
    private static final String NAME = String.format("%s:armor_material", RarmorAPI.MOD_ID);
    private static final int[] DURABILITY = new int[]{0, 0, 0, 0};
    private static final int[] DAMAGE_REDUCTION = new int[]{2, 5, 6, 2};
    private static final int ENCHANTABILITY = 0;
    public static final int TOUGHNESS = 0;
    
    private RarmorArmorMaterial(){}
    
    @Override
    public int getDurabilityForSlot(EquipmentSlot slotIn){
        return DURABILITY[slotIn.getIndex()];
    }
    
    @Override
    public int getDefenseForSlot(EquipmentSlot slotIn){
        return DAMAGE_REDUCTION[slotIn.getIndex()];
    }
    
    @Override
    public int getEnchantmentValue(){
        return ENCHANTABILITY;
    }
    
    @Override
    public SoundEvent getEquipSound(){
        return SoundEvents.ARMOR_EQUIP_CHAIN;
    }
    
    @Override
    public Ingredient getRepairIngredient(){
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
