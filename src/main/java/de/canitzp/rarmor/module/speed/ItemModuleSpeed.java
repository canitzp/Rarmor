/*
 * This file ("ItemModuleSpeed.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.speed;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.item.ItemRarmorModule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemModuleSpeed extends ItemRarmorModule {
    
    @Override
    public String[] getModuleIdentifiers(ItemStack stack){
        return new String[]{ActiveModuleSpeed.IDENTIFIER};
    }

    @Override
    public boolean canInstall(Player player, Slot slot, ItemStack stack, IRarmorData currentData){
        return RarmorAPI.methodHandler.getHasRarmorInSlot(player, EquipmentSlot.FEET) != null;
    }

    @Override
    public boolean canUninstall(Player player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
        tooltip.add(new TextComponent(ChatFormatting.ITALIC.toString()).append(new TranslatableComponent(RarmorAPI.MOD_ID+".needsShoes")));
    }
    
}
