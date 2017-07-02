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
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemModuleCompound extends ItemRarmorModule {

    private final String[] identifiers;
    private final ItemRarmorModule[] items;

    public ItemModuleCompound(String name, String[] identifiers, ItemRarmorModule[] items){
        super(name);
        this.identifiers = identifiers;
        this.items = items;
    }

    @Override
    public String[] getModuleIdentifiers(ItemStack stack){
        return this.identifiers;
    }

    @Override
    public boolean canInstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData){
        for(ItemRarmorModule module : this.items){
            if(!module.canInstall(player, slot, stack, currentData)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canUninstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData){
        for(ItemRarmorModule module : this.items){
            if(!module.canUninstall(player, slot, stack, currentData)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
        tooltip.add(TextFormatting.GOLD+I18n.format(RarmorAPI.MOD_ID+".compoundOf")+":");
        for(String id : this.identifiers){
            tooltip.add(TextFormatting.YELLOW+"   "+I18n.format("module."+id+".name"));
        }
        tooltip.add(TextFormatting.ITALIC+I18n.format(RarmorAPI.MOD_ID+".needsShoes"));
    }
}
