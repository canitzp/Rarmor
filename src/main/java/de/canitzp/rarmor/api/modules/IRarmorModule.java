/*
 * This file 'IRarmorModule.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.api.modules;

import de.canitzp.rarmor.api.InventoryBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
public interface IRarmorModule{

    /**
     * @return The unique name of the module. Example Generator: Name = 'moduleGenerator' UniqueName = 'Generator'
     */
    String getUniqueName();

    /**
     * @param player The player
     * @param stack The current stack/your module
     * @param advancedTooltips
     * @return This is addInformation in Item and only works if your module extends ItemModule
     */
    @SideOnly(Side.CLIENT)
    default String getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return null;
    }

    @SideOnly(Side.CLIENT)
    ModuleType getModuleType();

    /**
     * This is what get displayed while hovering over the question mark in the Rarmor Gui.
     * @return The hovering text.
     */
    @SideOnly(Side.CLIENT)
    default List<String> getGuiHelp(){
        return null;
    }

    default void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
    }

    default void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
    }

    @SideOnly(Side.CLIENT)
    default void initGui(Minecraft minecraft, ItemStack armorChestplate, GuiContainer gui){
    }

    @SideOnly(Side.CLIENT)
    default void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, boolean isColoringTab, float partialTicks, int mouseX, int mouseY){
    }

    @SideOnly(Side.CLIENT)
    default void drawScreen(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, boolean isColoringTab, float partialTicks, int mouseX, int mouseY){
    }

    default void onMouseClicked(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, boolean isColoringTab, int type, int mouseX, int mouseY){
    }

    default void onPlayerLoginEvent(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module){
    }

    @SideOnly(Side.CLIENT)
    default void onGuiOpenEvent(World worldObj, EntityPlayer player, ItemStack armorChestplate, ItemStack module, GuiContainer gui){
    }

    @SideOnly(Side.CLIENT)
    default void renderWorldScreen(Minecraft minecraft, EntityPlayer player, ItemStack armorChestplate, ItemStack module, ScaledResolution resolution, FontRenderer fontRendererObj, RenderGameOverlayEvent.ElementType type, float partialTicks){
    }

    /**
     * @param world           The World of the Player
     * @param player          The Player itself
     * @param armorChestplate The Rarmor Chestplate
     * @param damageSource    The Type of Damage the Player take
     * @param damage          The Amount of Damage th Player take
     * @return true if you want to cancel the Damage
     */
    default boolean onPlayerTakeDamage(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, DamageSource damageSource, float damage){
        return false;
    }

    default void onContainerTick(Container container, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
    }

    default void initModule(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
    }

    enum ModuleType{
        ACTIVE,
        PASSIVE,
        NONE
    }
}
