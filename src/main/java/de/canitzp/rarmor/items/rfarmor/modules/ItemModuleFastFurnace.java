/*
 * This file 'ItemModuleFastFurnace.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items.rfarmor.modules;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemModuleFastFurnace extends ItemModule implements IRarmorModule{

    public ItemModuleFastFurnace(){
        super("moduleFastFurnace");
    }

    @Override
    public String getUniqueName(){
        return "FastFurnace";
    }

    @Override
    public String getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return "The FastFurnace Module increases your Furnace speed by" + TextFormatting.BLUE + " 2" + TextFormatting.GRAY + "," +
                "but it also needs " + TextFormatting.BLUE + "300%" + TextFormatting.GRAY + " of the normal energy usage.";
    }

    @Override
    public ModuleType getModuleType(){
        return ModuleType.PASSIVE;
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        NBTUtil.setIntegerIfNot(armorChestplate, "rfPerTick", 60);
        NBTUtil.setIntegerIfNot(armorChestplate, "BurnTimeMultiplier", 2);
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, SlotUpdate slot){
        NBTUtil.setIntegerIfNot(armorChestplate, "rfPerTick", ItemRFArmorBody.rfPerTick);
        NBTUtil.setIntegerIfNot(armorChestplate, "BurnTimeMultiplier", 1);
    }

}
