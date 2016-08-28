/*
 * This file ("ItemModuleProtection.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.protection;

import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.item.ItemRarmorModule;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemModuleProtection extends ItemRarmorModule{

    public ItemModuleProtection(String name){
        super(name);

        this.setHasSubtypes(true);
    }

    @Override
    public String getModuleIdentifier(ItemStack stack){
        return ActiveModuleProtection.IDENTIFIERS[stack.getItemDamage()];
    }

    @Override
    public boolean canInstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData){
        for(String id : ActiveModuleProtection.IDENTIFIERS){
            if(currentData.getInstalledModuleWithId(id) != null){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canUninstall(EntityPlayer player, Slot slot, ItemStack stack, ActiveRarmorModule module, IRarmorData currentData){
        return true;
    }

    @Override
    public int getMetadata(int damage){
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack){
        return super.getUnlocalizedName(stack)+ActiveModuleProtection.TYPES[stack.getItemDamage()];
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems){
        for(int i = 0; i < ActiveModuleProtection.TYPES.length; i++){
            subItems.add(new ItemStack(item, 1, i));
        }
    }
}
