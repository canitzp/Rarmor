package de.canitzp.rarmor.module.color;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class ContainerModuleColor extends RarmorModuleContainer{

    private EntityPlayer player;

    public ContainerModuleColor(EntityPlayer player, Container container, ActiveRarmorModule module){
        super(container, module);
        this.player = player;
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<Slot>();

        this.addArmorSlotsAt(player, slots, 12, 20);
        this.addSecondHandSlot(player, slots, 12, 98);

        return slots;
    }

}
