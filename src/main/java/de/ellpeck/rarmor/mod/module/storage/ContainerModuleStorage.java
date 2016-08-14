/*
 * This file ("ContainerModuleStorage.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.storage;

import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class ContainerModuleStorage extends RarmorModuleContainer{

    public ContainerModuleStorage(Container container, ActiveRarmorModule module, IRarmorData currentData){
        super(container, module, currentData);
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<Slot>();

        ActiveModuleStorage module = (ActiveModuleStorage)this.module;
        for(int j = 0; j < 4; j++){
            for(int k = 0; k < 9; k++){
                slots.add(new Slot(module.inventory, k+j*9, 38+k*18, 26+j*18));
            }
        }

        return slots;
    }
}
