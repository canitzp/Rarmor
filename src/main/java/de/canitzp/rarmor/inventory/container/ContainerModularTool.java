/*
 * This file 'ContainerModularTool.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.inventory.container;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IToolModule;
import de.canitzp.rarmor.inventory.slots.SlotUpdate;
import de.canitzp.rarmor.items.modularTool.ItemModularTool;
import de.canitzp.rarmor.util.ContainerUtil;
import de.canitzp.rarmor.util.NBTUtil;
import de.canitzp.rarmor.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ContainerModularTool extends Container{

    public EntityPlayer player;
    public World world;
    public ItemStack armor, tool;
    public InventoryBase inventory;

    public ContainerModularTool(EntityPlayer player){
        this.player = player;
        this.world = player.worldObj;
        this.armor = PlayerUtil.getArmor(player, EntityEquipmentSlot.CHEST);
        this.tool = player.getHeldItemMainhand();
        this.inventory = NBTUtil.readSlots(this.tool, ItemModularTool.slots);

        for(int j = 0; j < 3; j++){
            this.addSlotToContainer(new SlotUpdate(this.inventory, j, 62 + j * 18, 8, player){
                @Override
                public boolean isItemValid(ItemStack stack){
                    return stack.getItem() instanceof IToolModule;
                }
            });
        }

        //Player Inventory:
        for(int j = 0; j < 3; ++j){
            for(int k = 0; k < 9; ++k){
                this.addSlotToContainer(new Slot(player.inventory, k + j * 9 + 9, 8 + k * 18, 29 + j * 18));
            }
        }
        //Player Hotbar:
        for(int j = 0; j < 9; ++j){
            this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 87));
        }
    }

    @Override
    public void detectAndSendChanges(){
        InventoryBase inv = NBTUtil.readSlots(this.tool, ItemModularTool.slots);
        this.inventory.slots = inv.slots;
        super.detectAndSendChanges();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn){
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        return ContainerUtil.transferStackInSlot(this.inventorySlots, player, slot);
    }

}
