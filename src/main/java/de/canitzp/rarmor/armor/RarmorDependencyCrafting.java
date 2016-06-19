package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Registry;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author canitzp
 */
public class RarmorDependencyCrafting extends ShapelessOreRecipe{

    private Pair<String, Boolean> nbtToSet;

    public RarmorDependencyCrafting(Pair<String, Boolean> nbtToSet, Object stack1, Object stack2, Object stack3){
        super(new ItemStack(Registry.rarmorChestplate), new ItemStack(Registry.rarmorChestplate), stack1, stack2, stack3);
        this.nbtToSet = nbtToSet;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1){
        ItemStack rarmor = null;
        for(int i = 0; i < var1.getSizeInventory(); i++){
            ItemStack stack = var1.getStackInSlot(i);
            if(stack != null && stack.getItem() == Registry.rarmorChestplate){
                rarmor = stack.copy();
                break;
            }
        }
        if(rarmor != null){
            NBTTagCompound oldTag = NBTUtil.getTagFromStack(rarmor);
            oldTag.setBoolean(this.nbtToSet.getKey(), this.nbtToSet.getValue());
            ItemStack newStack = new ItemStack(Registry.rarmorChestplate);
            NBTUtil.setTagFromStack(newStack, oldTag);
            return newStack;
        }
        return super.getCraftingResult(var1);
    }

}
