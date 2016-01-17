package de.canitzp.api.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class InventoryBase implements IInventory {

    public ItemStack[] slots;
    public String name;

    public InventoryBase(String name, int slotAmount){
        this.slots = new ItemStack[slotAmount];
        this.name = name;
    }

    @Override
    public int getSizeInventory() {
        return this.slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotId) {
        return slotId >= 0 && slotId < this.slots.length ? this.slots[slotId] : null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.slots[i] != null) {
            ItemStack itemstack;
            if (this.slots[i].stackSize <= j) {
                itemstack = this.slots[i];
                this.slots[i] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.slots[i].splitStack(j);
                if (this.slots[i].stackSize == 0) {
                    this.slots[i] = null;
                }
                this.markDirty();
                return itemstack;
            }
        } else return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotId) {
        if (this.slots[slotId] != null) {
            ItemStack itemstack = this.slots[slotId];
            this.slots[slotId] = null;
            return itemstack;
        } else return null;
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack stack) {
        this.slots[slotId] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return this.name;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }
}
