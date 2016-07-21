package de.canitzp.rarmor.api;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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
    default void onPacketData(EntityPlayer player, ItemStack stack, NBTTagCompound nbt){}

    default void onPacketBool(EntityPlayer player, int id, boolean bool){}

    /**
     * Container Manipulation
     */
    default void initContainer(Container container, EntityPlayer player){}

    default List<Slot> manipulateSlots(Container container, EntityPlayer player, List<Slot> slotList, InventoryBasic energyField, int containerOffsetLeft, int containerOffsetTop){return slotList;}

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

    @SideOnly(Side.CLIENT)
    default void onInWorldRendering(WorldClient world, EntityPlayerSP player, ItemStack stack, ScaledResolution resolution, FontRenderer fontRenderer, RenderGameOverlayEvent.ElementType type, float partialTicks, boolean isHelmet){}

    /**
     * Integration Events
     */
    /**
     * @param world The World
     * @param pos The position of the Atomic Reconstructor
     * @param stack The Rarmor Chestplate
     * @param oldColorValue The old color of the Rarmor Chestplate.
     * @param newColorValue The new color of the Rarmor Chestplate.
     */
    default void onArmorColorChangeThroughReconstructor(World world, BlockPos pos, ItemStack stack, int oldColorValue, int newColorValue){}
}
