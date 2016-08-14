/*
 * This file ("BasicInventory.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.inventory.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class BasicInventory implements IInventory{

    private final String name;
    private final ItemStack[] slots;

    public BasicInventory(String name, int slotAmount){
        this.name = name;
        this.slots = new ItemStack[slotAmount];
    }

    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public void markDirty(){

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player){
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player){

    }

    @Override
    public void closeInventory(EntityPlayer player){

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack){
        return true;
    }

    @Override
    public int getField(int id){
        return 0;
    }

    @Override
    public void setField(int id, int value){

    }

    @Override
    public int getFieldCount(){
        return 0;
    }

    @Override
    public void clear(){
        for(int i = 0; i < this.slots.length; i++){
            this.slots[i] = null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack stack){
        this.slots[i] = stack;
        this.markDirty();
    }

    @Override
    public int getSizeInventory(){
        return this.slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int i){
        if(i < this.getSizeInventory()){
            return this.slots[i];
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j){
        if(this.slots[i] != null){
            ItemStack stackAt;
            if(this.slots[i].stackSize <= j){
                stackAt = this.slots[i];
                this.slots[i] = null;
                this.markDirty();
                return stackAt;
            }
            else{
                stackAt = this.slots[i].splitStack(j);
                if(this.slots[i].stackSize <= 0){
                    this.slots[i] = null;
                }
                this.markDirty();
                return stackAt;
            }
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index){
        ItemStack stack = this.slots[index];
        this.slots[index] = null;
        return stack;
    }

    @Override
    public boolean hasCustomName(){
        return false;
    }

    @Override
    public ITextComponent getDisplayName(){
        return new TextComponentTranslation(this.getName());
    }

    public void saveSlots(NBTTagCompound compound){
        if(this.slots != null && this.slots.length > 0){
            NBTTagList tagList = new NBTTagList();
            for(ItemStack slot : this.slots){
                NBTTagCompound tagCompound = new NBTTagCompound();
                if(slot != null){
                    slot.writeToNBT(tagCompound);
                }
                tagList.appendTag(tagCompound);
            }
            compound.setTag("Items", tagList);
        }
    }

    public void loadSlots(NBTTagCompound compound){
        if(this.slots != null && this.slots.length > 0){
            NBTTagList tagList = compound.getTagList("Items", 10);
            for(int i = 0; i < this.slots.length; i++){
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                this.slots[i] = tagCompound != null && tagCompound.hasKey("id") ? ItemStack.loadItemStackFromNBT(tagCompound) : null;
            }
        }
    }

    public void drop(EntityPlayer player){
        if(!player.worldObj.isRemote){
            for(int i = 0; i < this.slots.length; i++){
                if(this.slots[i] != null){
                    player.dropItem(this.slots[i].copy(), false, false);
                    this.slots[i] = null;
                }
            }
        }
    }
}
