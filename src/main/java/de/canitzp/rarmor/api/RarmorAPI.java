/*
 * This file 'RarmorAPI.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.api;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.api.hudExtensions.IAdvancedHud;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class RarmorAPI{

    public static final String APIVERSION = "2";
    public static final String OWNER = "rarmor";
    public static final String PROVIDES = "rarmorapi";

    @SideOnly(Side.CLIENT)
    public static List<IAdvancedHud> hudExtension = new ArrayList<>();

    @SideOnly(Side.CLIENT)
    public static void addAdvancedHud(IAdvancedHud hud){
        hudExtension.add(hud);
    }

    public static ItemStack reduceRarmorEnergy(ItemStack armorChestplate, int toReduce){
        ((IEnergyContainerItem) armorChestplate.getItem()).extractEnergy(armorChestplate, toReduce, false);
        return armorChestplate;
    }

    public static ItemStack receiveRarmorEnergy(ItemStack armorChestplate, int toReceive){
        ((IEnergyContainerItem) armorChestplate.getItem()).receiveEnergy(armorChestplate, toReceive, false);
        return armorChestplate;
    }

    public static Colors addColor(String name, String colorValueName, int colorValue){
        return EnumHelper.addEnum(Colors.class, name, new Class[]{String.class, String.class, int.class}, name, colorValueName, colorValue);
    }
}
