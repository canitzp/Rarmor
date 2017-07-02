/*
 * This file ("ItemModuleSolar.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.solar;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.item.ItemRarmorModule;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemModuleSolar extends ItemRarmorModule{

    public ItemModuleSolar(String name){
        super(name);
    }

    @Override
    public String[] getModuleIdentifiers(ItemStack stack){
        return new String[]{ActiveModuleSolar.IDENTIFIER};
    }

    @Override
    public boolean canInstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData){
        return RarmorAPI.methodHandler.getHasRarmorInSlot(player, EntityEquipmentSlot.HEAD) != null;
    }

    @Override
    public boolean canUninstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
        tooltip.add(TextFormatting.ITALIC+I18n.format(RarmorAPI.MOD_ID+".needsHat"));
    }
}
