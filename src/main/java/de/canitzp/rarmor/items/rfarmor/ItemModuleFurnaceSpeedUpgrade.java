package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.inventory.slots.SlotModule;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
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
