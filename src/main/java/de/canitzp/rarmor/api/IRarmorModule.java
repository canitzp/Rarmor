package de.canitzp.rarmor.api;

import de.canitzp.rarmor.inventory.gui.GuiRFArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public interface IRarmorModule {

    String getUniqueName();

    void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory);

    void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory, Slot slot);

    void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY);

    void drawScreen(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY);

    void onMouseClicked(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY);

    boolean showSlot(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, Slot slot, int mouseX, int mouseY, int slotX, int slotY, boolean isMouseOverSlot);

}
