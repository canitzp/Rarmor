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
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public class ItemBattery extends ItemBase{

    public static final int CAPACITY = 500000;
    public static final int TRANSFER = 500;

    public ItemBattery(){
        super(new Properties().maxStackSize(1));
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
        tooltip.add(new StringTextComponent(TextFormatting.GOLD.toString()).append(new TranslationTextComponent(RarmorAPI.MOD_ID+".storedEnergy")).appendString(":"));
        tooltip.add(new StringTextComponent(TextFormatting.YELLOW+"   "+this.getEnergyStored(stack)+"/"+CAPACITY));
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack){
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        double max = CAPACITY;
        return (max-this.getEnergyStored(stack))/max;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT compound){
        return new ItemRarmorChest.CapProvider(stack, CAPACITY, TRANSFER, TRANSFER);
    }
    
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
        super.fillItemGroup(group, items);
        
        if(this.isInGroup(group)){
            ItemStack stack = new ItemStack(this);
            Helper.setItemEnergy(stack, CAPACITY);
            items.add(stack);
        }
    }
    
    public int getEnergyStored(ItemStack stack){
        if(stack.hasTag()){
            return stack.getTag().getInt("Energy");
        }
        return 0;
    }

}
