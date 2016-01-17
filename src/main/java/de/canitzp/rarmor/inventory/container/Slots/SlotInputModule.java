package de.canitzp.rarmor.inventory.container.Slots;

import de.canitzp.api.inventory.InventoryBase;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.items.rfarmor.ItemModuleGenerator;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmor;
import de.canitzp.api.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class SlotInputModule extends SlotArmorInventory {

    private final EntityPlayer player;

    public SlotInputModule(InventoryBase inventory, int id, int x, int y, EntityPlayer player) {
        super(inventory, id, x, y, player);
        this.player = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemModule;
    }

    @Override
    public void onSlotChanged(){
        ItemStack stack = player.getCurrentArmor(ItemRFArmor.ArmorType.BODY.getId()+1);
        if(getStack() != null && getStack().getItem() != null){
            if(getStack().getItem() instanceof ItemModuleGenerator){
                NBTUtil.setBoolean(stack, "ModuleGenerator", true);
            } else {
                NBTUtil.setBoolean(stack, "ModuleGenerator", false);
            }
        }
        super.onSlotChanged();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        ItemStack armor = player.getCurrentArmor(ItemRFArmor.ArmorType.BODY.getId()+1);
        NBTUtil.setBoolean(stack, "ModuleGenerator", false);
        dropSlot(this.inventory.getStackInSlotOnClosing(30), player, armor);
        NBTUtil.setInteger(armor, "GenBurnTime", 0);
        super.onPickupFromSlot(player, stack);
    }

    private void dropSlot(ItemStack stack, EntityPlayer player, ItemStack armor) {
        if(stack != null){
            if(!player.worldObj.isRemote){
                player.dropPlayerItemWithRandomChoice(stack, false);
            }
            this.inventory.setInventorySlotContents(30, null);
            NBTUtil.saveSlots(armor, (InventoryBase) inventory);
        }
    }

}
