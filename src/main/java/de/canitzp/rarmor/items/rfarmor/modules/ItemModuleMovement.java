/*
 * This file 'ItemModuleMovement.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items.rfarmor.modules;

import de.canitzp.rarmor.RarmorProperties;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.util.EnergyUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemModuleMovement extends ItemModule implements IRarmorModule{

    public static int energyPerTick;

    public ItemModuleMovement(){
        super("moduleMovement");
        energyPerTick = RarmorProperties.getInteger("moduleMovementEnergyPerTick");
    }

    @Override
    public String getUniqueName(){
        return "Movement";
    }

    @Override
    public String getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return "The Movement Module increase your walk and fly speed and disables the fall damage.\nIt costs " + TextFormatting.RED + energyPerTick + "RF" + TextFormatting.GRAY + " per Tick.";
    }

    @Override
    public ModuleType getModuleType(){
        return ModuleType.PASSIVE;
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        if(!world.isRemote){
            player.capabilities.disableDamage = true;
            player.capabilities.setPlayerWalkSpeed(0.4F);
        } else {
            player.capabilities.setFlySpeed(0.1F);
        }
        EnergyUtil.reduceEnergy(armorChestplate, energyPerTick);
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        if(!world.isRemote){
            player.capabilities.disableDamage = false;
            player.capabilities.setPlayerWalkSpeed(0.1F);
        } else {
            player.capabilities.setFlySpeed(0.05F);
        }
        player.sendPlayerAbilities();
    }
}
