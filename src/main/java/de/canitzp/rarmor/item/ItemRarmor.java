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
import de.canitzp.rarmor.misc.CreativeTab;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ItemRarmor extends DyeableArmorItem {

    public static final int DEFAULT_ARMOR_COLOR = 0xFFFFFFFF;

    public ItemRarmor(EquipmentSlot slot){
        super(RarmorArmorMaterial.INSTANCE, slot, new Properties().tab(CreativeTab.INSTANCE));
    }

    @Override
    public Rarity getRarity(ItemStack stack){
        return Rarity.EPIC;
    }

    public static int getArmorColor(ItemStack stack){
        if(!stack.isEmpty() && stack.getItem() instanceof ItemRarmor item){
            if(item.hasCustomColor(stack)){
                return item.getColor(stack);
            }
        }
        return DEFAULT_ARMOR_COLOR;
    }

    public static void setArmorColor(ItemStack stack, int color){
        if(!stack.isEmpty() && stack.getItem() instanceof ItemRarmor item){
            item.setColor(stack, color);
        }
    }

}
