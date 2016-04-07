package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.RarmorProperties;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.util.EnergyUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemModuleFlying extends ItemModule implements IRarmorModule {

    private int energyUsagePerTick;

    public ItemModuleFlying() {
        super("moduleFlying");
        this.energyUsagePerTick = RarmorProperties.getInteger("moduleFlyingEnergyPerTick");
    }

    @Override
    public String getUniqueName() {
        return "Flying";
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory) {
        if (!player.capabilities.isCreativeMode) {
            if (!NBTUtil.getBoolean(module, "deactivated")) {
                if (EnergyUtil.getEnergy(armorChestplate) >= this.energyUsagePerTick) {
                    player.capabilities.allowFlying = true;
                    if (player.capabilities.isFlying) {
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
            } else if (EnergyUtil.getEnergy(armorChestplate) >= this.energyUsagePerTick) {
                NBTUtil.setBoolean(module, "deactivated", false);
            }
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory, Slot slot) {
        if (!player.capabilities.isCreativeMode) {
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
            player.capabilities.disableDamage = false;
        }
    }

    @Override
    public void onPlayerLoginEvent(World world, EntityPlayer player, ItemStack playersRarmorChestplate, ItemStack module) {
        if (!NBTUtil.getBoolean(module, "deactivated")) {
            player.capabilities.allowFlying = true;
            player.capabilities.isFlying = true;
            player.sendPlayerAbilities();
        }
    }

}
