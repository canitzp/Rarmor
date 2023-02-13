/*
 * This file ("ItemBattery.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.item;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public class ItemBattery extends ItemBase{

    public static final int CAPACITY = 500000;
    public static final int TRANSFER = 500;

    public ItemBattery(){
        super(new Properties().stacksTo(1));
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
        tooltip.add(new TextComponent(ChatFormatting.GOLD.toString()).append(new TranslatableComponent(RarmorAPI.MOD_ID+".storedEnergy")).append(":"));
        tooltip.add(new TextComponent(ChatFormatting.YELLOW+"   "+this.getEnergyStored(stack)+"/"+CAPACITY));
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack){
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return ((CAPACITY-this.getEnergyStored(stack))/CAPACITY) * 13;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag compound){
        return new ItemRarmorChest.CapProvider(stack, CAPACITY, TRANSFER, TRANSFER);
    }
    
    public int getEnergyStored(ItemStack stack){
        if(stack.hasTag()){
            return stack.getTag().getInt("Energy");
        }
        return 0;
    }

}
