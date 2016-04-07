package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotModuleSplitter extends SlotUpdate {

    public SlotModuleSplitter(IInventory inventory, int id, int x, int y, ItemStack saveStack) {
        super(inventory, id, x, y, saveStack);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof IRarmorModule;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        ItemStack mod1 = this.inventory.getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onPickupFromSlot(player.getEntityWorld(), player, RarmorUtil.getPlayersRarmorChestplate(player), stack, this.inventory, this);
        }
        ItemStack mod2 = this.inventory.getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onPickupFromSlot(player.getEntityWorld(), player, RarmorUtil.getPlayersRarmorChestplate(player), stack, this.inventory, this);
        }
        ItemStack mod3 = this.inventory.getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onPickupFromSlot(player.getEntityWorld(), player, RarmorUtil.getPlayersRarmorChestplate(player), stack, this.inventory, this);
        }
    }
}
