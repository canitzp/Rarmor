package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemModuleFastFurnace extends ItemModule implements IRarmorModule {

    public ItemModuleFastFurnace() {
        super("moduleFastFurnace");
    }

    @Override
    public String getUniqueName() {
        return "FastFurnace";
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory) {
        NBTUtil.setIntegerIfNot(armorChestplate, "rfPerTick", 60);
        NBTUtil.setIntegerIfNot(armorChestplate, "BurnTimeMultiplier", 2);
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory, Slot slot) {
        NBTUtil.setIntegerIfNot(armorChestplate, "rfPerTick", ItemRFArmorBody.rfPerTick);
        NBTUtil.setIntegerIfNot(armorChestplate, "BurnTimeMultiplier", 1);
    }

}
