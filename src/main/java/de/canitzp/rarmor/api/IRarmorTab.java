package de.canitzp.rarmor.api;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
    @NotNull String getTabIdentifier(ItemStack rarmor, EntityPlayer player);

    default boolean canBeVisible(ItemStack rarmor, EntityPlayer player){return true;}

    default NBTTagCompound writeToNBT(NBTTagCompound nbt){return nbt;}

    default void readFromNBT(NBTTagCompound nbt){}

    @SideOnly(Side.CLIENT)
    default ItemStack getTabIcon(){return null;}

    @SideOnly(Side.CLIENT)
    default void drawTab(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop){}

    @SideOnly(Side.CLIENT)
    default String getTabHoveringText(){return null;}

    /**
     * Container Manipulation
     */
    default void initContainer(Container container, EntityPlayer player){}

    default List<Slot> manipulateSlots(Container container, EntityPlayer player, List<Slot> slotList, int containerOffsetLeft, int containerOffsetTop){return slotList;}

    /**
     * Just Gui things
     */
    @SideOnly(Side.CLIENT)
    default void initGui(GuiContainer gui, EntityPlayer player){}

    @SideOnly(Side.CLIENT)
    default void drawGui(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){}

    @SideOnly(Side.CLIENT)
    default void drawForeground(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){}

    @SideOnly(Side.CLIENT)
    default void onMouseClick(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, int btnID){}

}
