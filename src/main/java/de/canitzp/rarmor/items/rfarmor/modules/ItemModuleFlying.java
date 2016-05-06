/*
 * This file 'ItemModuleFlying.java' is part of Rarmor by canitzp.
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
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemModuleFlying extends ItemModule implements IRarmorModule{

    private int energyUsagePerTick;

    public ItemModuleFlying(){
        super("moduleFlying");
        this.energyUsagePerTick = RarmorProperties.getInteger("moduleFlyingEnergyPerTick");
    }

    @Override
    public String getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return "With this Module you can grab the stars in CreativeFlightMode. It uses " + TextFormatting.RED + this.energyUsagePerTick + TextFormatting.GRAY + "RF per Tick while you're flying.";
    }

    @Override
    public String getUniqueName(){
        return "Flying";
    }

    @Override
    public ModuleType getModuleType(){
        return ModuleType.PASSIVE;
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        if(!player.capabilities.isCreativeMode){
            if(!NBTUtil.getBoolean(module, "deactivated")){
                if(EnergyUtil.getEnergy(armorChestplate) >= this.energyUsagePerTick){
                    player.capabilities.allowFlying = true;
                    if(player.capabilities.isFlying){
                        EnergyUtil.reduceEnergy(armorChestplate, this.energyUsagePerTick);
                        NBTUtil.setBoolean(module, "deactivated", false);
                    } else {
                        NBTUtil.setBoolean(module, "deactivated", true);
                    }
                } else {
                    player.capabilities.allowFlying = false;
                    player.capabilities.isFlying = false;
                    player.capabilities.disableDamage = false;
                    NBTUtil.setBoolean(module, "deactivated", true);
                }
            } else if(EnergyUtil.getEnergy(armorChestplate) >= this.energyUsagePerTick){
                NBTUtil.setBoolean(module, "deactivated", false);
            }
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        if(!player.capabilities.isCreativeMode){
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
            player.capabilities.disableDamage = false;
            player.sendPlayerAbilities();
        }
    }

    @Override
    public void onPlayerLoginEvent(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module){
        if(!NBTUtil.getBoolean(module, "deactivated")){
            player.capabilities.allowFlying = true;
            player.capabilities.isFlying = true;
            player.sendPlayerAbilities();
        }
    }

}
