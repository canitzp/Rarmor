package de.canitzp.rarmor.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
public interface IRarmorModule {

    String getUniqueName();

    default void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory){}

    default void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory, Slot slot){}

    @SideOnly(Side.CLIENT)
    default void initGui(World world, EntityPlayer player, ItemStack armorChestplate, GuiContainer gui, List<GuiCheckBox> checkBoxes, ResourceLocation checkBoxResource){}

    @SideOnly(Side.CLIENT)
    default void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY, int guiLeft, int guiTop){}

    @SideOnly(Side.CLIENT)
    default void drawScreen(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY){}

    @SideOnly(Side.CLIENT)
    default void onMouseClicked(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY, int guiLeft, int guiTop) {}

    @SideOnly(Side.CLIENT)
    default boolean showSlot(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, Slot slot, int mouseX, int mouseY, int slotX, int slotY, boolean isMouseOverSlot) {
        return isMouseOverSlot && !(slot instanceof IModuleSlot);
    }

    default void onPlayerLoginEvent(World world, EntityPlayer player, ItemStack module){}

    @SideOnly(Side.CLIENT)
    default void onGuiOpenEvent(World worldObj, EntityPlayer player, GuiScreen gui, ItemStack module){}

    @SideOnly(Side.CLIENT)
    default void renderWorldScreen(Minecraft minecraft, EntityPlayer player, ScaledResolution resolution, FontRenderer fontRendererObj, RenderGameOverlayEvent.ElementType type, ItemStack module, float partialTicks){}

    /**
     *
     * @param world The World of the Player
     * @param player The Player itself
     * @param armor The Rarmor Chestplate
     * @param damageSource The Type of Damage the Player take
     * @param damage The Amount of Damage th Player take
     * @return true if you want to cancel the Damage
     */
    default boolean onPlayerTakeDamage(World world, EntityPlayer player, ItemStack armor, DamageSource damageSource, float damage){return false;}

}
