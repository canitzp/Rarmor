package de.canitzp.rarmor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author canitzp
 */
public class CompatUtil {

    public static boolean isEmpty(ItemStack stack){
        /* 1.10.2: */ return stack == null || stack.getItem() == null;
        /* 1.11.2: */ //return stack.isEmpty();
    }

    public static void growStack(ItemStack stack, int amount){
        /* 1.10.2: */ stack.stackSize += amount;
        /* 1.11.2: */ //stack.grow(amount);
    }

    public static void shrinkStack(ItemStack stack, int amount){
        /* 1.10.2: */ stack.stackSize -= amount;
        /* 1.11.2: */ //stack.shrink(amount);
    }

    public static int getStackCount(ItemStack stack){
        /* 1.10.2 */ return !isEmpty(stack) ? stack.stackSize : 0;
        /* 1.11.2 */ //return stack.getCount();
    }

    public static ItemStack getEmpty(){
        /* 1.10.2 */ return null;
        /* 1.11.2 */ //return ItemStack.EMPTY;
    }

    public static ItemStack fromNBT(NBTTagCompound nbt){
        /* 1.10.2 */ return ItemStack.func_77949_a(nbt);
        /* 1.11.2 */ //return new ItemStack(nbt);
    }

    public static void onTakeSlot(Slot slot, EntityPlayer player, ItemStack stack){
        /* 1.10.2: */ slot.func_82870_a(player, stack);
        /* 1.11.2: */ //slot.onTake(player, stack);
    }

}
