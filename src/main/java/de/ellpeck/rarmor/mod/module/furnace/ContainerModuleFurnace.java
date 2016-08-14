/*
 * This file ("ContainerModuleEnder.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.furnace;

import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;

import java.util.ArrayList;
import java.util.List;

public class ContainerModuleFurnace extends RarmorModuleContainer{

    private final EntityPlayer player;

    public ContainerModuleFurnace(EntityPlayer player, Container container, ActiveRarmorModule module){
        super(container, module);
        this.player = player;
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<Slot>();

        ActiveModuleFurnace module = (ActiveModuleFurnace)this.module;
        slots.add(new Slot(module.inventory, 0, 82, 58));
        slots.add(new SlotFurnaceOutput(this.player, module.inventory, 1, 132, 58));

        return slots;
    }
}
