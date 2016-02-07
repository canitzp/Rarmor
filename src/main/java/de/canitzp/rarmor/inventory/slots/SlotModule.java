package de.canitzp.rarmor.inventory.slots;

import de.canitzp.rarmor.api.IModuleSlot;
import de.canitzp.rarmor.api.SlotUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class SlotModule extends SlotUpdate implements IModuleSlot{

    public boolean active;

    public SlotModule(IInventory inventory, int id, int x, int y, EntityPlayer player) {
        super(inventory, id, x, y, player);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isSlotActive() {
        return active;
    }

    @Override
    public void setSlotActive() {
        this.active = true;
    }

    @Override
    public void setSlotInactive() {
        this.active = false;
    }

}
