/*
 * This file 'ItemModuleIronPickaxe.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items.modularTool;

import de.canitzp.rarmor.api.modules.IToolModule;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class ItemModuleIronPickaxe extends ItemModule implements IToolModule{

    public ItemModuleIronPickaxe(){
        super("pickaxeIron");
    }

    @Override
    public String getName(){
        return "PickaxeIron";
    }

    @Override
    public float getStrengthAgainstBlock(ItemStack module, IBlockState state, ItemStack stack){
        if(state.getBlock().isToolEffective("pickaxe", state)){
            return ToolMaterial.IRON.getEfficiencyOnProperMaterial();
        }
        return super.getStrVsBlock(stack, state);
    }

    @Override
    public boolean canHarvestBlock(ItemStack module, IBlockState state, ItemStack stack){
        return state.getBlock().isToolEffective("pickaxe", state);
    }
}
