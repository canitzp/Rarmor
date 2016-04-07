package de.canitzp.rarmor.inventory.container;

import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import de.canitzp.rarmor.inventory.slots.*;
import de.canitzp.rarmor.inventory.slots.SlotFurnaceOutput;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketSyncPlayerHotbar;
import de.canitzp.rarmor.util.ContainerUtil;
import de.canitzp.rarmor.util.NBTUtil;
import de.canitzp.rarmor.util.PlayerUtil;
import de.canitzp.rarmor.util.inventory.InventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

/**
 * @author canitzp
 */
public class ContainerRFArmor extends Container {

    public InventoryBase inventory;
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    public EntityPlayer player;
    public SlotUpdate generatorSlot;
    public ItemStack armor;

    public ContainerRFArmor(EntityPlayer player) {
        if (player.worldObj.isRemote)
            NetworkHandler.wrapper.sendToServer(new PacketSyncPlayerHotbar(player));
        this.armor = PlayerUtil.getArmor(player, EntityEquipmentSlot.CHEST);
        InventoryPlayer inventoryPlayer = player.inventory;
        this.inventory = NBTUtil.readSlotsBase(this.armor, ItemRFArmorBody.slotAmount);
        this.player = player;
        armor.getTagCompound().setBoolean("click", false);

        //Armor Inventory:
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new SlotUpdate(this.inventory, j + i * 9, 44 + j * 18, 87 + i * 18, this.armor));
            }
        }
        //Player Inventory:
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventoryPlayer, k + j * 9 + 9, 44 + k * 18, 144 + j * 18));
            }
        }
        //Player Hotbar:
        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryPlayer, j, 44 + j * 18, 202));
        }
        //Armor Crafting Grid:
        this.addSlotToContainer(new SlotCraftingOutput(player, this.craftMatrix, this.craftResult, 0, 217, 99));
        for (int l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 3; ++i1) {
                this.addSlotToContainer(new SlotCraftingInput(this.craftMatrix, i1 + l * 3, 180 + i1 * 18, 14 + l * 18));
            }
        }
        //Armor Furnace Input:
        this.addSlotToContainer(new SlotUpdate(this.inventory, 27, 15, 57, this.armor));
        //Armor Furnace Output:
        this.addSlotToContainer(new SlotFurnaceOutput(this.inventory, 28, 15, 98, player, this.armor));
        //Armor Module Slot:
        this.addSlotToContainer(new SlotInputModule(this.inventory, 29, 15, 34, player, this.armor));

        addSlotToContainer(new SlotUnmovable(player.inventory, 39, 44, 10));
        addSlotToContainer(new SlotUnmovable(player.inventory, 38, 44, 10 + 18));
        addSlotToContainer(new SlotUnmovable(player.inventory, 37, 44, 10 + 18 * 2));
        addSlotToContainer(new SlotUnmovable(player.inventory, 36, 44, 10 + 18 * 3));

        this.generatorSlot = new SlotModule(this.inventory, 30, 140, 18, this.armor);
        this.addSlotToContainer(this.generatorSlot);

        this.onCraftMatrixChanged(this.craftMatrix);

        ItemStack module = inventory.getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if (module != null) {
            if (module.getItem() instanceof IRarmorModule) {
                ((IRarmorModule) module.getItem()).onContainerIsInit(this, player, inventory, this.armor, module);
            }
        }

    }

    @Override
    public void detectAndSendChanges() {
        InventoryBase inv = NBTUtil.readSlotsBase(this.armor, ItemRFArmorBody.slotAmount);
        this.inventory.slots = inv.slots;
        ItemStack module = NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if (module != null) {
            if (module.getItem() instanceof IRarmorModule) {
                ((IRarmorModule) module.getItem()).onContainerTick(this, player, inventory, this.armor, module);
            }
        }
        super.detectAndSendChanges();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.inventory.isUseableByPlayer(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        dropCraftMatrix(player);
        super.onContainerClosed(player);
    }

    private void dropCraftMatrix(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
                if (itemstack != null) {
                    player.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        return ContainerUtil.transferStackInSlot(this.inventorySlots, player, slot);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.player.worldObj));
        super.onCraftMatrixChanged(inventory);
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot) {
        if (slot instanceof SlotCrafting || slot instanceof SlotCraftingInput) {
            return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
        }
        return super.canMergeSlot(stack, slot);
    }

    public void addSlot(Slot slot){
        this.addSlotToContainer(slot);
    }

}
