/*
 * This file 'ContainerRFArmor.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.inventory.container;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.inventory.slots.SlotFurnaceOutput;
import de.canitzp.rarmor.inventory.slots.*;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketSyncPlayerHotbar;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class ContainerRFArmor extends Container{

    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    public InventoryBase inventory;
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    public EntityPlayer player;
    public SlotUpdate generatorSlot;
    public SlotModuleSplitterInput sideSlot1, sideSlot2, sideSlot3;
    public ItemStack armor;

    public ContainerRFArmor(EntityPlayer player){
        if(player.worldObj.isRemote)
            NetworkHandler.wrapper.sendToServer(new PacketSyncPlayerHotbar(player));
        this.armor = RarmorUtil.getPlayersRarmorChestplate(player);
        InventoryPlayer inventoryPlayer = player.inventory;
        this.inventory = RarmorUtil.readRarmor(player);
        this.player = player;
        armor.getTagCompound().setBoolean("click", false);

        //Armor Inventory: 0-26
        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 9; j++){
                this.addSlotToContainer(new SlotUpdate(this.inventory, j + i * 9, 44 + j * 18, 87 + i * 18, player));
            }
        }
        //Player Inventory: 27-53
        for(int j = 0; j < 3; ++j){
            for(int k = 0; k < 9; ++k){
                this.addSlotToContainer(new Slot(inventoryPlayer, k + j * 9 + 9, 44 + k * 18, 144 + j * 18));
            }
        }
        //Player Hotbar: 54-62
        for(int j = 0; j < 9; ++j){
            this.addSlotToContainer(new Slot(inventoryPlayer, j, 44 + j * 18, 202));
        }
        //Armor Crafting Grid: 63 + 64-72
        this.addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 217, 99));
        for(int l = 0; l < 3; ++l){
            for(int i1 = 0; i1 < 3; ++i1){
                this.addSlotToContainer(new Slot(this.craftMatrix, i1 + l * 3, 180 + i1 * 18, 14 + l * 18));
            }
        }
        //Armor Furnace Input: 73
        this.addSlotToContainer(new SlotUpdate(this.inventory, 27, 15, 57, player));
        //Armor Furnace Output: 74
        this.addSlotToContainer(new SlotFurnaceOutput(this.inventory, 28, 15, 98, player));
        //Armor Module Slot: 75
        this.addSlotToContainer(new SlotInputModule(this.inventory, 29, 15, 34, player));

        //Armor Slots: 76-79
        for (int k = 0; k < 4; ++k) {
            final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[k];
            this.addSlotToContainer(new SlotUnmovable(player.inventory, 36 + (3 - k), 44, 10 + k * 18) {
                public int getSlotStackLimit()
                {
                    return 1;
                }
                public boolean isItemValid(ItemStack stack) {
                    return stack != null && stack.getItem().isValidArmor(stack, entityequipmentslot, player);
                }
                @SideOnly(Side.CLIENT)
                public String getSlotTexture()
                {
                    return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
                }
            });
        }

        //Generic Slot Single: 80
        this.addSlotToContainer(this.generatorSlot = new SlotUpdate(this.inventory, 30, 140, 18, player));

        //Side Slots: 81, 82, 83
        addSlotToContainer(this.sideSlot1 = new SlotModuleSplitterInput(inventory, 31, -16, 14, player));
        addSlotToContainer(this.sideSlot2 = new SlotModuleSplitterInput(inventory, 32, -16, 34, player));
        addSlotToContainer(this.sideSlot3 = new SlotModuleSplitterInput(inventory, 33, -16, 54, player));

        //Shield Slot: 84
        this.addSlotToContainer(new Slot(player.inventory, 40, 116, 64) {
            public boolean isItemValid(ItemStack stack)
            {
                return super.isItemValid(stack);
            }
            @SideOnly(Side.CLIENT)
            public String getSlotTexture()
            {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void detectAndSendChanges(){
        this.inventory.slots = RarmorUtil.readRarmor(player).slots;
        super.detectAndSendChanges();
        /*for (int i = 0; i < this.inventorySlots.size(); ++i) {
            ItemStack itemstack = this.inventorySlots.get(i).getStack();
            ItemStack itemstack1 = this.inventory.getStackInSlot(i);
            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
                itemstack1 = itemstack == null ? null : itemstack.copy();
                this.inventoryItemStacks.set(i, itemstack1);
                for (ICrafting listener : this.listeners) {
                    listener.sendSlotContents(this, i, itemstack1);
                }
            }
        }*/

        ItemStack module = inventory.getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if(module != null){
            if(module.getItem() instanceof IRarmorModule){
                ((IRarmorModule) module.getItem()).onContainerTick(this, player, this.armor, module, inventory);
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player){
        return this.inventory.isUseableByPlayer(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        NBTUtil.saveSlots(this.armor, this.inventory);
        dropCraftMatrix(player);
        super.onContainerClosed(player);
    }

    private void dropCraftMatrix(EntityPlayer player){
        if(!player.worldObj.isRemote){
            for(int i = 0; i < 9; ++i){
                ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
                if(itemstack != null){
                    player.dropItem(itemstack, false);
                }
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex){
        int armorInvStart = 0, armorInvLength = 26; //0-26
        int invStart = 27, invLength = 26; //27-53
        int hotbarStart = 54, hotbarLength = 8; //54-62
        int craftStart = 64, craftLength = 8, craftOut = 63; // 63 & 64-72
        int furnaceInput = 73, furnaceOutput = 74; // 73 & 74
        int mod0 = 75;
        Slot slot = this.inventorySlots.get(slotIndex);
        ItemStack stack = null;
        if(slot != null && slot.getHasStack()){
            stack = slot.getStack();
            ItemStack stack1 = stack.copy();
            if(slotIndex >= invStart && slotIndex <= invStart + invLength){
                if(!this.mergeItemStack(stack, hotbarStart, hotbarStart + hotbarLength, false)){
                    return null;
                }
            } else if(slotIndex >= hotbarStart && slotIndex <= hotbarStart + hotbarLength){
                if(!this.mergeItemStack(stack, invStart, invStart + invLength, false)){
                    return null;
                }
            } else if(slotIndex >= armorInvStart && slotIndex <= armorInvStart + armorInvLength){
                if(!this.mergeItemStack(stack, invStart, invStart + invLength, false)){
                    return null;
                }
            } else if(slotIndex == craftOut){
                if(!this.mergeItemStack(stack1, invStart, invStart + invLength, true)){
                    return null;
                }
                slot.onSlotChange(stack1, stack);
            } else if(slotIndex >= craftStart && slotIndex <= craftStart + craftLength){
                if(!this.mergeItemStack(stack, invStart, invStart + invLength, false)){
                    return null;
                }
            } else if(slotIndex == mod0){
                if(!this.mergeItemStack(stack1, invStart, invStart + invLength, false)){
                    return null;
                }
            } else if(slotIndex == furnaceInput || slotIndex == furnaceOutput){
                if(!this.mergeItemStack(stack1, invStart, invStart + invLength, false)){
                    return null;
                }
            }
            if(stack1.stackSize == 0){
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if(stack1.stackSize == stack.stackSize){
                return null;
            }
            slot.onPickupFromSlot(player, stack1);
        }
        return stack;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory){
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.player.worldObj));
        super.onCraftMatrixChanged(inventory);
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot){
        if(slot instanceof SlotCrafting){
            return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
        }
        return super.canMergeSlot(stack, slot);
    }

}
