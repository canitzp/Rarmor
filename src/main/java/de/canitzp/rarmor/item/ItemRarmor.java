/*
 * This file ("ItemRarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import de.canitzp.rarmor.IRenderItem;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.misc.CreativeTab;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.awt.*;
import java.util.Locale;

public class ItemRarmor extends ItemArmor implements IRenderItem {

    private static final ArmorMaterial RARMOR_MATERIAL = EnumHelper.addArmorMaterial(RarmorAPI.MOD_ID.toUpperCase(Locale.ROOT)+"_MATERIAL", RarmorAPI.MOD_ID+":rarmor", 0, new int[]{2, 5, 6, 2}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);

    public ItemRarmor(String name, EntityEquipmentSlot slot){
        super(RARMOR_MATERIAL, -1, slot);

        this.setRegistryName(RarmorAPI.MOD_ID, name);
        ItemBase.ITEMS_TO_REGISTER.add(this);

        this.setTranslationKey(this.getRegistryName().toString());
        this.setCreativeTab(CreativeTab.INSTANCE);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.EPIC;
    }

    @Override
    public boolean hasColor(ItemStack stack){
        return stack.hasTagCompound() && stack.getTagCompound().getInteger("Color") != 0;
    }

    @Override
    public int getColor(ItemStack stack){
        return stack.hasTagCompound() ? stack.getTagCompound().hasKey("Color") ? stack.getTagCompound().getInteger("Color") : super.getColor(stack) : super.getColor(stack);
    }

    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
