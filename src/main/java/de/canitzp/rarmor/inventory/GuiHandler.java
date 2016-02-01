package de.canitzp.rarmor.inventory;

import de.canitzp.rarmor.inventory.container.ContainerCircuitCreator;
import de.canitzp.rarmor.inventory.container.ContainerRFArmor;
import de.canitzp.rarmor.inventory.gui.GuiCircuitCreator;
import de.canitzp.rarmor.inventory.gui.GuiRFArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author canitzp
 */
public class GuiHandler implements IGuiHandler {

    public static final int RFARMORGUI = 0, CIRCUITCREATOR = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case RFARMORGUI:
                return new ContainerRFArmor(player);
            case CIRCUITCREATOR:
                return new ContainerCircuitCreator(player);
            default: return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case RFARMORGUI:
                return new GuiRFArmor(player, new ContainerRFArmor(player));
            case CIRCUITCREATOR:
                return new GuiCircuitCreator(player, new ContainerCircuitCreator(player));
            default: return null;
        }
    }
}
