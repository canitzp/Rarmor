package de.canitzp.rarmor.util.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

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
    public ItemStack removeStackFromSlot(int index) {
        return null;
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
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty(){}

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }
}
