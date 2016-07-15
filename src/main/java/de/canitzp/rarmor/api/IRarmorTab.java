package de.canitzp.rarmor.api;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
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
    String getTabIdentifier(ItemStack rarmor, EntityPlayer player);

    default boolean canBeVisible(ItemStack rarmor, EntityPlayer player){return true;}

    default NBTTagCompound writeToNBT(NBTTagCompound nbt){return nbt;}

    default void readFromNBT(NBTTagCompound nbt){}

    @SideOnly(Side.CLIENT)
    default ItemStack getTabIcon(){return null;}

    @SideOnly(Side.CLIENT)
    default void drawTab(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop){}

    @SideOnly(Side.CLIENT)
    default String getTabHoveringText(){return null;}

    default void tick(World world, EntityPlayer player, ItemStack stack){}

    default NBTTagCompound getPacketData(EntityPlayerMP player, ItemStack stack){return new NBTTagCompound();}

    @SideOnly(Side.CLIENT)
    default void onPacketData(NBTTagCompound nbt){}

    /**
     * Container Manipulation
     */
    default void initContainer(Container container, EntityPlayer player){}

    default List<Slot> manipulateSlots(Container container, EntityPlayer player, List<Slot> slotList, InventoryBasic energyField, int containerOffsetLeft, int containerOffsetTop){return slotList;}

    default void addListener(Container container, EntityPlayer player, IContainerListener listener){}

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

    @SideOnly(Side.CLIENT)
    default void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){}

}
