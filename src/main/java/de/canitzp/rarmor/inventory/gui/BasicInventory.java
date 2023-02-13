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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemStackHandler;

public class BasicInventory extends ItemStackHandler {

    private final IRarmorData data;

    public BasicInventory(int slotAmount, IRarmorData data){
        super(slotAmount);
        this.data = data;
    }

    public void drop(Entity entity){
        if(!entity.getLevel().isClientSide()){
            for(int i = 0; i < this.getSlots(); i++){
                this.dropSingle(entity, i);
            }
        }
    }

    public void dropSingle(Entity entity, int i){
        if(!this.getStackInSlot(i).isEmpty()){
            Block.popResource(entity.level, entity.blockPosition(), this.getStackInSlot(i).copy());
            this.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
