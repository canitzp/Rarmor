/*
 * This file ("ItemRarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import de.canitzp.rarmor.RarmorArmorMaterial;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.misc.CreativeTab;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

public class ItemRarmor extends DyeableArmorItem {
    
    public ItemRarmor(String name, EquipmentSlotType slot){
        super(RarmorArmorMaterial.INSTANCE, slot, new Properties().group(CreativeTab.INSTANCE));

        this.setRegistryName(RarmorAPI.MOD_ID, name);
    }

    @Override
    public Rarity getRarity(ItemStack stack){
        return Rarity.EPIC;
    }

    
    @Override
    public boolean hasColor(ItemStack stack){
        return stack.hasTag() && stack.getTag().getInt("Color") != 0;
    }

    @Override
    public int getColor(ItemStack stack){
        return stack.hasTag() ? stack.getTag().contains("Color") ? stack.getTag().getInt("Color") : super.getColor(stack) : super.getColor(stack);
    }
}
