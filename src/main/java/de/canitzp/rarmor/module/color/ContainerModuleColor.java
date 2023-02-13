package de.canitzp.rarmor.module.color;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class ContainerModuleColor extends RarmorModuleContainer{

    private Player player;

    public ContainerModuleColor(Player player, AbstractContainerMenu container, ActiveRarmorModule module){
        super(container, module);
        this.player = player;
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<>();

        this.addArmorSlotsAt(player, slots, 12, 20);
        this.addSecondHandSlot(player, slots, 12, 98);

        return slots;
    }

}
