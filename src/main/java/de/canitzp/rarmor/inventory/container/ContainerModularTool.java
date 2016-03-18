package de.canitzp.rarmor.inventory.container;

import de.canitzp.rarmor.api.SlotUpdate;
import de.canitzp.rarmor.items.ItemModularTool;
import de.canitzp.rarmor.util.ContainerUtil;
import de.canitzp.rarmor.util.NBTUtil;
import de.canitzp.rarmor.util.inventory.InventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ContainerModularTool extends Container {

    public EntityPlayer player;
    public World world;
    public ItemStack armor, tool;
    public InventoryBase inventory;

    public ContainerModularTool(EntityPlayer player){
        this.player = player;
        this.world = player.worldObj;
        this.armor = player.inventory.armorInventory[2];
        this.tool = player.getActiveItemStack();
        this.inventory = NBTUtil.readSlotsBase(this.tool, ItemModularTool.slots);

        for(int j = 0; j < 3; j++){
            this.addSlotToContainer(new SlotUpdate(this.inventory, j, 44 + j * 18, 87, this.tool));
        }

        //Player Inventory:
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(player.inventory, k + j * 9 + 9, 44 + k * 18, 144 + j * 18));
            }
        }
        //Player Hotbar:
        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(player.inventory, j, 44 + j * 18, 202));
        }
    }

    @Override
    public void detectAndSendChanges() {
        InventoryBase inv = NBTUtil.readSlotsBase(this.tool, ItemModularTool.slots);
        this.inventory.slots = inv.slots;
        super.detectAndSendChanges();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        return ContainerUtil.transferStackInSlot(this.inventorySlots, player, slot);
    }

}
