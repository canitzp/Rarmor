/*
 * This file ("ContainerModuleEnder.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.ender;

import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class ContainerModuleEnder extends RarmorModuleContainer{

    private final EntityPlayer player;

    public ContainerModuleEnder(EntityPlayer player, Container container, ActiveRarmorModule module, IRarmorData currentData){
        super(container, module, currentData);
        this.player = player;
    }

    @Override
    public List<Slot> getSlots(){
        List<Slot> slots = new ArrayList<Slot>();

        InventoryEnderChest ender = this.player.getInventoryEnderChest();
        for(int j = 0; j < 3; j++){
            for(int k = 0; k < 9; k++){
                slots.add(new Slot(ender, k+j*9, 38+k*18, 26+j*18));
            }
        }


        return slots;
    }
}
