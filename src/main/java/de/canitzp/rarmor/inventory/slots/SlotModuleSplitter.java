package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import de.canitzp.rarmor.items.ItemRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotModuleSplitter extends SlotUpdate {

    public SlotModuleSplitter(InventoryBase inventory, int id, int x, int y, ItemStack saveStack) {
        super(inventory, id, x, y, saveStack);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof IRarmorModule && stack.getItem() != ItemRegistry.moduleModuleSplitter;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        ((IRarmorModule) stack.getItem()).onPickupFromSlot(player.getEntityWorld(), player, RarmorUtil.getPlayersRarmorChestplate(player), stack, this.inventory, this);
    }
}
