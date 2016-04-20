package de.canitzp.rarmor.inventory.container;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.container.ContainerBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import de.canitzp.rarmor.inventory.slots.*;
import de.canitzp.rarmor.inventory.slots.SlotFurnaceOutput;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketSyncPlayerHotbar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

/**
 * @author canitzp
 */
public class ContainerRFArmor extends ContainerBase{

    public InventoryBase inventory;
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    public EntityPlayer player;
    public SlotUpdate generatorSlot;
    public SlotModuleSplitterInput sideSlot1, sideSlot2, sideSlot3;
    public ItemStack armor;

    public ContainerRFArmor(EntityPlayer player){
        if (player.worldObj.isRemote)
            NetworkHandler.wrapper.sendToServer(new PacketSyncPlayerHotbar(player));
        this.armor = RarmorUtil.getPlayersRarmorChestplate(player);
        InventoryPlayer inventoryPlayer = player.inventory;
        this.inventory = RarmorUtil.readRarmor(player);
        RarmorUtil.saveRarmor(player, this.inventory);
        this.player = player;
        armor.getTagCompound().setBoolean("click", false);

        //Armor Inventory: 0-26
        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 9; j++){
                this.addSlotToContainer(new SlotUpdate(this.inventory, j + i * 9, 44 + j * 18, 87 + i * 18, player));
            }
        }
        //Player Inventory: 27-53
        for (int j = 0; j < 3; ++j){
            for (int k = 0; k < 9; ++k){
                this.addSlotToContainer(new Slot(inventoryPlayer, k + j * 9 + 9, 44 + k * 18, 144 + j * 18));
            }
        }
        //Player Hotbar: 54-62
        for (int j = 0; j < 9; ++j){
            this.addSlotToContainer(new Slot(inventoryPlayer, j, 44 + j * 18, 202));
        }
        //Armor Crafting Grid: 63 + 64-72
        this.addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 217, 99));
        for (int l = 0; l < 3; ++l){
            for (int i1 = 0; i1 < 3; ++i1){
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
        addSlotToContainer(new SlotUnmovable(player.inventory, 39, 44, 10));
        addSlotToContainer(new SlotUnmovable(player.inventory, 38, 44, 10 + 18));
        addSlotToContainer(new SlotUnmovable(player.inventory, 37, 44, 10 + 18 * 2));
        addSlotToContainer(new SlotUnmovable(player.inventory, 36, 44, 10 + 18 * 3));

        //Generic Slot Single: 80
        this.addSlotToContainer(this.generatorSlot = new SlotUpdate(this.inventory, 30, 140, 18, player));

        addSlotToContainer(this.sideSlot1 = new SlotModuleSplitterInput(inventory, 31, -16, 14, player));
        addSlotToContainer(this.sideSlot2 = new SlotModuleSplitterInput(inventory, 32, -16, 34, player));
        addSlotToContainer(this.sideSlot3 = new SlotModuleSplitterInput(inventory, 33, -16, 54, player));

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void detectAndSendChanges(){
        InventoryBase inv = RarmorUtil.readRarmor(player);
        this.inventory.slots = inv.slots;
        super.detectAndSendChanges();
        ItemStack module = inv.getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if (module != null){
            if (module.getItem() instanceof IRarmorModule){
                ((IRarmorModule) module.getItem()).onContainerTick(this, player, inventory, this.armor, module);
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player){
        return this.inventory.isUseableByPlayer(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        dropCraftMatrix(player);
        super.onContainerClosed(player);
    }

    private void dropCraftMatrix(EntityPlayer player){
        if (!player.worldObj.isRemote){
            for (int i = 0; i < 9; ++i){
                ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
                if (itemstack != null){
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
        if (slot != null && slot.getHasStack()){
            stack = slot.getStack();
            ItemStack stack1 = stack.copy();
            if (slotIndex >= invStart && slotIndex <= invStart + invLength){
                if (!this.mergeItemStack(stack, hotbarStart, hotbarStart + hotbarLength, false)){
                    return null;
                }
            }else if (slotIndex >= hotbarStart && slotIndex <= hotbarStart + hotbarLength){
                if (!this.mergeItemStack(stack, invStart, invStart + invLength, false)){
                    return null;
                }
            }else if (slotIndex >= armorInvStart && slotIndex <= armorInvStart + armorInvLength){
                if (!this.mergeItemStack(stack, invStart, invStart + invLength, false)){
                    return null;
                }
            }else if (slotIndex == craftOut){
                if (!this.mergeItemStack(stack1, invStart, invStart + invLength, true)){
                    return null;
                }
                slot.onSlotChange(stack1, stack);
            }else if(slotIndex >= craftStart && slotIndex <= craftStart + craftLength){
                if (!this.mergeItemStack(stack, invStart, invStart + invLength, false)){
                    return null;
                }
            } else if(slotIndex == mod0){
                if (!this.mergeItemStack(stack1, invStart, invStart + invLength, false)){
                    return null;
                }
            } else if(slotIndex == furnaceInput || slotIndex == furnaceOutput){
                if (!this.mergeItemStack(stack1, invStart, invStart + invLength, false)){
                    return null;
                }
            }
            if (stack1.stackSize == 0){
                slot.putStack(null);
            }else{
                slot.onSlotChanged();
            }

            if (stack1.stackSize == stack.stackSize){
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
        if (slot instanceof SlotCrafting || slot instanceof SlotCraftingInput){
            return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
        }
        return super.canMergeSlot(stack, slot);
    }

}
