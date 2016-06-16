package de.canitzp.rarmor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author canitzp
 */
public class NBTUtil{

    public static void setInteger(ItemStack stack, int i, String key){
        if(stack != null){
            if(stack.hasTagCompound()){
                stack.getTagCompound().setInteger(key, i);
            } else {
                stack.setTagCompound(new NBTTagCompound());
                setInteger(stack, i, key);
            }
        }
    }

    public static int getInteger(ItemStack stack, String key){
        if(stack != null && stack.hasTagCompound()){
            return stack.getTagCompound().getInteger(key);
        }
        return 0;
    }

    public static void setEnergy(ItemStack stack, int energy){
        setInteger(stack, energy, "Energy");
    }

    public static int getEnergy(ItemStack stack){
        return getInteger(stack, "Energy");
    }

    public static NBTTagCompound getTagFromStack(ItemStack stack){
        if(stack != null){
            if(!stack.hasTagCompound()){
                stack.setTagCompound(new NBTTagCompound());
            }
            return stack.getTagCompound();
        }
        return null;
    }

    public static void setTagFromStack(ItemStack stack, NBTTagCompound nbt){
        if(stack != null && nbt != null){
            stack.setTagCompound(nbt);
        }
    }

}
