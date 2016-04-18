package de.canitzp.rarmor.api.container;

import de.canitzp.rarmor.api.slots.ISpecialSlot;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

/**
 * @author canitzp
 */
public abstract class ContainerBase extends Container {

    public void addSlot(Slot slot){
        this.addSlotToContainer(slot);
    }

    public int getNextSlotId(){
        return inventorySlots.size() + 1;
    }

    public void toggleSlot(ISpecialSlot specialSlot){
        specialSlot.setSlotExist(!specialSlot.doesSlotExist());
    }

    public void setSlot(ISpecialSlot slot, boolean value){
        slot.setSlotExist(value);
    }

}
