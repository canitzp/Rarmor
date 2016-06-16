package de.canitzp.rarmor.api;

import com.sun.istack.internal.NotNull;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
public interface IRarmorTab{

    /**
     * Tab stuff:
     */

    @NotNull String getTabIdentifier();

    default ItemStack getTabIcon(){return null;}

    default void drawTab(GuiContainer gui, int guiLeft, int guiTop){}

    default String getTabHoveringText(){return null;}

    /**
     * Container Manipulation
     */

    default void initContainer(Container container, EntityPlayer player){}

    default List<Slot> manipulateSlots(Container container, List<Slot> slotList){return slotList;}

    /**
     * Just Gui things
     */

    @SideOnly(Side.CLIENT)
    default void initGui(GuiContainer gui, EntityPlayer player){}

    @SideOnly(Side.CLIENT)
    default void drawGui(GuiContainer gui, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){

    }

}
