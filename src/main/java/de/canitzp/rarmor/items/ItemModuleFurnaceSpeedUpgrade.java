package de.canitzp.rarmor.items;

import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.util.inventory.InventoryBase;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemModuleFurnaceSpeedUpgrade extends ItemModule implements IRarmorModule {

    public ItemModuleFurnaceSpeedUpgrade() {
        super("moduleFastFurnace");
    }

    @Override
    public String getUniqueName() {
        return "FastFurnace";
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory) {
        NBTUtil.setIntegerIfNot(armorChestplate, "rfPerTick", 60);
        NBTUtil.setIntegerIfNot(armorChestplate, "BurnTimeMultiplier", 2);
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory) {
        NBTUtil.setIntegerIfNot(armorChestplate, "rfPerTick", ((ItemRFArmorBody)armorChestplate.getItem()).rfPerTick);
        NBTUtil.setIntegerIfNot(armorChestplate, "BurnTimeMultiplier", 1);
    }
}
