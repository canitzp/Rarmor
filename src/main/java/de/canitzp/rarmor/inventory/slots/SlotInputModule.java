package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.api.SlotUpdate;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmor;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotInputModule extends SlotUpdate {

    private final EntityPlayer player;
    private ItemStack actualStack;

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
        if(actualStack != null && !actualStack.isItemEqual(this.getStack())){
            if(actualStack.getItem() != null && actualStack.getItem() instanceof IRarmorModule){
                ((IRarmorModule) actualStack.getItem()).onPickupFromSlot(player.getEntityWorld(), player, player.getCurrentArmor(2), stack, this.inventory, this);
            }
        }
        if(getStack() != null && getStack().getItem() != null){
            if(getStack().getItem() instanceof IRarmorModule){
                NBTUtil.setBoolean(stack, "Module" + ((IRarmorModule) getStack().getItem()).getUniqueName(), true);
            }
        }
        actualStack = this.getStack();
        super.onSlotChanged();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        if(stack.getItem() instanceof IRarmorModule){
            IRarmorModule module = (IRarmorModule) stack.getItem();
            module.onPickupFromSlot(player.getEntityWorld(), player, player.getCurrentArmor(2), stack, this.inventory, this);
            NBTUtil.setBoolean(stack, "Module" + module.getUniqueName(), false);
        }
        actualStack = null;
    }

}
