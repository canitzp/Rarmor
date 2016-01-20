package de.canitzp.rarmor.inventory.container.Slots;

import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.util.inventory.InventoryBase;
import de.canitzp.util.util.NBTUtil;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotInputModule extends SlotArmorInventory {

    private final EntityPlayer player;

    public SlotInputModule(IInventory inventory, int id, int x, int y, EntityPlayer player) {
        super(inventory, id, x, y, player);
        this.player = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof IRarmorModule;
    }

    @Override
    public void onSlotChanged(){
        ItemStack stack = player.getCurrentArmor(ItemRFArmor.ArmorType.BODY.getId()+1);
        if(getStack() != null && getStack().getItem() != null){
            if(getStack().getItem() instanceof IRarmorModule){
                NBTUtil.setBoolean(stack, "Module" + ((IRarmorModule) getStack().getItem()).getUniqueName(), true);
            }
        }
        super.onSlotChanged();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        if(stack.getItem() instanceof IRarmorModule){
            IRarmorModule module = (IRarmorModule) stack.getItem();
            NBTUtil.setBoolean(stack, "Module" + module.getUniqueName(), false);
            module.onPickupFromSlot(player.getEntityWorld(), player, player.getCurrentArmor(2), stack, this.inventory);
        }

    }

}
