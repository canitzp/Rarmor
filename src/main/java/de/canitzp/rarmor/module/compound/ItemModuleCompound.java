/*
 * This file ("ItemModuleCompound.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.compound;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.item.ItemRarmorModule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemModuleCompound extends ItemRarmorModule {

    private final String[] identifiers;
    private final ItemRarmorModule[] items;

    public ItemModuleCompound(String[] identifiers, ItemRarmorModule[] items){
        this.identifiers = identifiers;
        this.items = items;
    }

    @Override
    public String[] getModuleIdentifiers(ItemStack stack){
        return this.identifiers;
    }

    @Override
    public boolean canInstall(Player player, Slot slot, ItemStack stack, IRarmorData currentData){
        for(ItemRarmorModule module : this.items){
            if(!module.canInstall(player, slot, stack, currentData)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canUninstall(Player player, Slot slot, ItemStack stack, IRarmorData currentData){
        for(ItemRarmorModule module : this.items){
            if(!module.canUninstall(player, slot, stack, currentData)){
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
        tooltip.add(new TextComponent(ChatFormatting.GOLD.toString()).append(new TranslatableComponent(RarmorAPI.MOD_ID+".compoundOf")));
        for(String id : this.identifiers){
            tooltip.add(new TextComponent(ChatFormatting.YELLOW.toString() + "   ").append(new TranslatableComponent("module."+id+".name")));
        }
        tooltip.add(new TextComponent(ChatFormatting.ITALIC.toString()).append(new TranslatableComponent(RarmorAPI.MOD_ID+".needsShoes")));
    }
    
}
