/*
 * This file ("ContainerModuleMain.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.module.main;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.module.IActiveRarmorModule;
import de.canitzp.rarmor.mod.inventory.gui.slot.SlotModule;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class ContainerModuleMain extends RarmorModuleContainer{

    public ContainerModuleMain(IActiveRarmorModule module){
        super(module);
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<Slot>();

        ActiveModuleMain module = (ActiveModuleMain)this.module;
        for(int i = 0; i < 3; i++){
            slots.add(new SlotModule(module.inventory, i, 11, 10+i*26));
        }

        return slots;
    }
}
