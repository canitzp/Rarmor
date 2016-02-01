package de.canitzp.rarmor.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

/**
 * @author canitzp
 */
public class ContainerCircuitCreator extends Container {

    public EntityPlayer player;

    public ContainerCircuitCreator(EntityPlayer player){
        this.player = player;
        //Player Inventory:
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(player.inventory, k + j * 9 + 9, 90 + k * 18, 176 + j * 18));
            }
        }
        //Player Hotbar:
        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(player.inventory, j, 90 + j * 18, 234));
        }
    }
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

}
