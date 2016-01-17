package de.canitzp.rarmor.inventory.container.Slots;

import de.canitzp.api.inventory.InventoryBase;
import de.canitzp.rarmor.inventory.ArmorInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

/**
 * @author canitzp
 */
public class SlotModule extends SlotArmorInventory {

    public SlotModule(InventoryBase inventory, int id, int x, int y, EntityPlayer player) {
        super(inventory, id, x, y, player);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return TileEntityFurnace.isItemFuel(stack);
    }

}
