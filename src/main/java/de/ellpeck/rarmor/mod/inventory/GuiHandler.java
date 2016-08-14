/*
 * This file ("GuiHandler.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.inventory;

import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.Rarmor;
import de.ellpeck.rarmor.mod.data.RarmorData;
import de.ellpeck.rarmor.mod.inventory.gui.GuiRarmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler{

    public GuiHandler(){
        NetworkRegistry.INSTANCE.registerGuiHandler(Rarmor.instance, this);
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z){
        IRarmorData data = RarmorData.getDataForChestplate(player);
        ActiveRarmorModule module = this.getModuleToOpen(data);
        if(module != null && module.hasTab(player)){
            return new ContainerRarmor(player, module);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z){
        IRarmorData data = RarmorData.getDataForChestplate(player);
        ActiveRarmorModule module = this.getModuleToOpen(data);
        if(module != null && module.hasTab(player)){
            return new GuiRarmor(new ContainerRarmor(player, module), module);
        }
        return null;
    }

    private ActiveRarmorModule getModuleToOpen(IRarmorData data){
        if(data != null){
            return data.getCurrentModules().get(data.getCurrentModules().size() <= data.getSelectedModule() ? 0 : data.getSelectedModule());
        }
        return null;
    }
}
