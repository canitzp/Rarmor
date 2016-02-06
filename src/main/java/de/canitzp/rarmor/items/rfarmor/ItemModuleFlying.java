package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.inventory.slots.SlotModule;
import de.canitzp.util.util.EnergyUtil;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemModuleFlying extends ItemModule implements IRarmorModule {

    public ItemModuleFlying() {
        super("moduleFlying");
    }

    @Override
    public String getUniqueName() {
        return "Flying";
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory) {
        if(!player.capabilities.isCreativeMode){
            if(!NBTUtil.getBoolean(module, "deactivated")){
                if(EnergyUtil.getEnergy(armorChestplate) >= 5){
                    player.capabilities.allowFlying = true;
                    if(player.capabilities.isFlying){
                        EnergyUtil.reduceEnergy(armorChestplate, 5);
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
            } else if(EnergyUtil.getEnergy(armorChestplate) >= 5){
                NBTUtil.setBoolean(module, "deactivated", false);
            }
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory, Slot slot) {
        if(!player.capabilities.isCreativeMode){
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
            player.capabilities.disableDamage = false;
        }
    }

    @Override
    public void onPlayerLoginEvent(World world, EntityPlayer player, ItemStack module){
        if(!NBTUtil.getBoolean(module, "deactivated")){
            player.capabilities.allowFlying = true;
            player.capabilities.isFlying = true;
            player.sendPlayerAbilities();
        }
    }

}
