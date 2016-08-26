/*
 * This file ("ContainerModuleGenerator.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.generator;

import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import java.util.Collections;
import java.util.List;

public class ContainerModuleGenerator extends RarmorModuleContainer{

    public ContainerModuleGenerator(Container container, ActiveRarmorModule module){
        super(container, module);
    }

    @Override
    public List<Slot> getSlots(){
        return Collections.singletonList(new Slot(((ActiveModuleGenerator)this.module).inventory, 0, 110, 65));
    }
}
