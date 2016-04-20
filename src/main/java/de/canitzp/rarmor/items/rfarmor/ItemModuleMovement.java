package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemModuleMovement extends ItemModule implements IRarmorModule{

    public ItemModuleMovement(){
        super("moduleMovement");
    }

    @Override
    public String getUniqueName(){
        return "Movement";
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        if(!world.isRemote){
            player.capabilities.disableDamage = true;
            if(player.isSprinting()){
                player.capabilities.setPlayerWalkSpeed(0.5F);
            } else {
                player.capabilities.setPlayerWalkSpeed(0.1F);
            }
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, SlotUpdate slot){
        if(!world.isRemote){
            player.capabilities.disableDamage = false;
            player.capabilities.setPlayerWalkSpeed(0.1F);
        }
    }
}
