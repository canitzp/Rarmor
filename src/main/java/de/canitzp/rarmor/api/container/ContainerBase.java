package de.canitzp.rarmor.api.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

/**
 * @author canitzp
 */
public abstract class ContainerBase extends Container {

    public void addSlot(Slot slot){
        this.addSlotToContainer(slot);
    }

}
