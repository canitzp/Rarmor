package de.canitzp.rarmor.network;

import de.canitzp.rarmor.armor.GuiContainerRarmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author canitzp
 */
public class GuiHandler implements IGuiHandler{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        return new GuiContainerRarmor(player);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        return new GuiContainerRarmor.GuiRarmor(player);
    }
}
