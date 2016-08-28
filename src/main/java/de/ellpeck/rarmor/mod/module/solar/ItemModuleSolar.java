/*
 * This file ("ItemModuleSolar.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.solar;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.item.ItemRarmorModule;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class ItemModuleSolar extends ItemRarmorModule{

    public ItemModuleSolar(String name){
        super(name);
    }

    @Override
    public String getModuleIdentifier(ItemStack stack){
        return ActiveModuleSolar.IDENTIFIER;
    }

    @Override
    public boolean canInstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData){
        return RarmorAPI.methodHandler.getHasRarmorInSlot(player, EntityEquipmentSlot.HEAD) != null;
    }

    @Override
    public boolean canUninstall(EntityPlayer player, Slot slot, ItemStack stack, ActiveRarmorModule module, IRarmorData currentData){
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
        tooltip.add(TextFormatting.ITALIC+I18n.format(RarmorAPI.MOD_ID+".needsHat"));
    }
}
