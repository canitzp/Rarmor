package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.api.SlotUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

/**
 * @author canitzp
 */
public class SlotModule extends SlotUpdate {

    public SlotModule(IInventory inventory, int id, int x, int y, EntityPlayer player) {
        super(inventory, id, x, y, player);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return TileEntityFurnace.isItemFuel(stack);
    }

}
