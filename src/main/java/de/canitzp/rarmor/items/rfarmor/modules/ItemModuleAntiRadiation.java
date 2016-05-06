/*
 * This file 'ItemModuleAntiRadiation.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items.rfarmor.modules;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

/**
 * @author canitzp
 */
public class ItemModuleAntiRadiation extends ItemModule implements IRarmorModule{

    private boolean isModuleLoaded;

    public ItemModuleAntiRadiation(){
        super("moduleAntiRadiation");
        this.isModuleLoaded = Loader.isModLoaded("deepresonance");
    }

    @Override
    public String getUniqueName(){
        return "AntiRadiation";
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        armorChestplate.getTagCompound().removeTag("AntiRadiationArmor");
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        NBTUtil.setBoolean(armorChestplate, "AntiRadiationArmor", true);
    }
}
