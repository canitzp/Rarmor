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
import de.canitzp.rarmor.misc.CreativeTab;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBattery extends ItemBase{

    public static final int CAPACITY = 500000;
    public static final int TRANSFER = 500;

    public ItemBattery(String name){
        super(name);
        this.setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
        tooltip.add(TextFormatting.GOLD+I18n.format(RarmorAPI.MOD_ID+".storedEnergy")+":");
        tooltip.add(TextFormatting.YELLOW+"   "+this.getEnergyStored(stack)+"/"+CAPACITY);
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
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound compound){
        return new ItemRarmorChest.CapProvider(stack, CAPACITY, TRANSFER, TRANSFER);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems){
        super.getSubItems(tab, subItems);

        if(this.isInCreativeTab(tab)){
            ItemStack stack = new ItemStack(this);
            Helper.setItemEnergy(stack, CAPACITY);
            subItems.add(stack);
        }
    }

    public int getEnergyStored(ItemStack stack){
        if(stack.hasTagCompound()){
            return stack.getTagCompound().getInteger("Energy");
        }
        return 0;
    }

}
