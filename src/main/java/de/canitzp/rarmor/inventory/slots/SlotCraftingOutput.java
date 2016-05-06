/*
 * This file 'SlotCraftingOutput.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.inventory.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;

/**
 * @author canitzp
 */
public class SlotCraftingOutput extends SlotCrafting{

    private boolean isSlot;

    public SlotCraftingOutput(EntityPlayer player, InventoryCrafting craftingInventory, IInventory p_i45790_3_, int slotIndex, int xPosition, int yPosition){
        super(player, craftingInventory, p_i45790_3_, slotIndex, xPosition, yPosition);
    }

}
