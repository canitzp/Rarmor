/*
 * This file ("GuiHandler.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.inventory;

import de.canitzp.rarmor.api.module.IActiveRarmorModule;
import de.canitzp.rarmor.mod.Rarmor;
import de.canitzp.rarmor.mod.data.RarmorData;
import de.canitzp.rarmor.mod.inventory.gui.GuiRarmor;
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
        IActiveRarmorModule module = this.getModuleToOpen(player);
        if(module != null && module.hasTab()){
            return new ContainerRarmor(player, module);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z){
        IActiveRarmorModule module = this.getModuleToOpen(player);
        if(module != null && module.hasTab()){
            return new GuiRarmor(new ContainerRarmor(player, module), module);
        }
        return null;
    }

    private IActiveRarmorModule getModuleToOpen(EntityPlayer player){
        RarmorData data = RarmorData.getDataForChestplate(player);
        if(data != null){
            return data.loadedModules.get(data.loadedModules.size() <= data.guiToOpen ? 0 : data.guiToOpen);
        }
        return null;
    }
}
