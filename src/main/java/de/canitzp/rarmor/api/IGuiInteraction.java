package de.canitzp.rarmor.api;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public interface IGuiInteraction {

    default void initGui(GuiScreen gui, World world, EntityPlayer player){}

    default void drawGuiContainerBackgroundLayer(GuiScreen gui, World world, EntityPlayer player, int guiLeft, int guiTop, float partialTicks, int mouseX, int mouseY){}

    default void drawScreen(GuiScreen gui, World world, EntityPlayer player, int mouseX, int mouseY, float partialTicks){}

    default void mouseClicked(GuiScreen gui, World world, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, int mouseButton) throws IOException {}

    default void mouseHover(GuiScreen gui, World world, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY){}

    default void drawGuiContainerForegroundLayer(GuiScreen gui, World world, EntityPlayer player, int mouseX, int mouseY) {}

    default boolean onKeyPressed(GuiScreen gui, World world, EntityPlayer player, char typedChar, int keyCode){return false;}

}
