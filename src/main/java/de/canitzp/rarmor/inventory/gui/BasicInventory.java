/*
 * This file ("BasicInventory.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.inventory.gui;

import de.canitzp.rarmor.api.internal.IRarmorData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;

public class BasicInventory implements IInventory{

    private final IRarmorData data;
    private final String name;
    private final NonNullList<ItemStack> slots;

    public BasicInventory(String name, int slotAmount, IRarmorData data){
        this.name = name;
        this.slots = NonNullList.withSize(slotAmount, ItemStack.EMPTY);
        this.data = data;
    }

    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public void markDirty(){
        this.data.setDirty();
    }
    
    @Override
    public boolean isUsableByPlayer(PlayerEntity player){
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack){
        return true;
    }

    @Override
    public void clear(){
        for(int i = 0; i < this.slots.size(); i++){
            this.slots.set(i, ItemStack.EMPTY);
        }
        this.markDirty();
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack stack){
        this.slots.set(i, stack);
        this.markDirty();
    }

    @Override
    public int getSizeInventory(){
        return this.slots.size();
    }

    @Override
    public boolean isEmpty(){
        for (ItemStack itemstack : this.slots){
            if (!itemstack.isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int i){
        if(i < this.getSizeInventory()){
            return this.slots.get(i);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int i, int j){
        if(!this.slots.get(i).isEmpty()){
            ItemStack stackAt;
            if(this.slots.get(i).getCount() <= j){
                stackAt = this.slots.get(i);
                this.slots.set(i, ItemStack.EMPTY);
                this.markDirty();
                return stackAt;
            }
            else{
                stackAt = this.slots.get(i).split(j);
                if(this.slots.get(i).getCount() <= 0){
                    this.slots.set(i, ItemStack.EMPTY);
                }
                this.markDirty();
                return stackAt;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index){
        ItemStack stack = this.slots.get(index);
        this.slots.set(index, ItemStack.EMPTY);
        return stack;
    }

    public void saveSlots(CompoundNBT compound){
        if(this.slots != null && this.slots.size() > 0){
            ListNBT tagList = new ListNBT();
            for(ItemStack slot : this.slots){
                CompoundNBT tagCompound = new CompoundNBT();
                if(slot != null){
                    slot.write(tagCompound);
                }
                tagList.add(tagCompound);
            }
            compound.put("Items", tagList);
        }
    }

    public void loadSlots(CompoundNBT compound){
        if(this.slots != null && this.slots.size() > 0){
            ListNBT tagList = compound.getList("Items", 10);
            for(int i = 0; i < this.slots.size(); i++){
                CompoundNBT tagCompound = tagList.getCompound(i);
                this.slots.set(i, tagCompound != null && tagCompound.contains("id") ? ItemStack.read(tagCompound) : ItemStack.EMPTY);
            }
        }
    }

    public void drop(Entity entity){
        if(!entity.getEntityWorld().isRemote){
            for(int i = 0; i < this.slots.size(); i++){
                this.dropSingle(entity, i);
            }
        }
    }

    public void dropSingle(Entity entity, int i){
        if(!this.slots.get(i).isEmpty()){
            entity.entityDropItem(this.slots.get(i).copy(), 0);
            this.slots.set(i, ItemStack.EMPTY);

            this.markDirty();
        }
    }
}
