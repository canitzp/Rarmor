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
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

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
    public boolean canInstall(PlayerEntity player, Slot slot, ItemStack stack, IRarmorData currentData){
        for(ItemRarmorModule module : this.items){
            if(!module.canInstall(player, slot, stack, currentData)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canUninstall(PlayerEntity player, Slot slot, ItemStack stack, IRarmorData currentData){
        for(ItemRarmorModule module : this.items){
            if(!module.canUninstall(player, slot, stack, currentData)){
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
        tooltip.add(new StringTextComponent(TextFormatting.GOLD.toString()).append(new TranslationTextComponent(RarmorAPI.MOD_ID+".compoundOf")));
        for(String id : this.identifiers){
            tooltip.add(new StringTextComponent(TextFormatting.YELLOW.toString() + "   ").append(new TranslationTextComponent("module."+id+".name")));
        }
        tooltip.add(new StringTextComponent(TextFormatting.ITALIC.toString()).append(new TranslationTextComponent(RarmorAPI.MOD_ID+".needsShoes")));
    }
    
}
