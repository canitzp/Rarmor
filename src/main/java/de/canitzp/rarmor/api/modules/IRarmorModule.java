package de.canitzp.rarmor.api.modules;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.container.ContainerBase;
import de.canitzp.rarmor.api.gui.GuiCheckBox;
import de.canitzp.rarmor.api.gui.GuiContainerBase;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
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
public interface IRarmorModule{

    String getUniqueName();

    default List<String> getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return null;
    }

    default ModuleType getModuleType(){
        return ModuleType.NONE;
    }

    default List<String> getGuiHelp(){
        return null;
    }

    default void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
    }

    default void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, SlotUpdate slot){
    }

    @SideOnly(Side.CLIENT)
    default void initGui(World world, EntityPlayer player, ItemStack armorChestplate, GuiContainerBase gui, List<GuiCheckBox> checkBoxes, ResourceLocation checkBoxResource){
    }

    @SideOnly(Side.CLIENT)
    default void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY, int guiLeft, int guiTop){
    }

    @SideOnly(Side.CLIENT)
    default void drawScreen(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY){
    }

    @SideOnly(Side.CLIENT)
    default void onMouseClicked(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY, int guiLeft, int guiTop){
    }

    default void onPlayerLoginEvent(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module){
    }

    @SideOnly(Side.CLIENT)
    default void onGuiOpenEvent(World worldObj, EntityPlayer player, GuiScreen gui, ItemStack armorChestplate, ItemStack module){
    }

    @SideOnly(Side.CLIENT)
    default void renderWorldScreen(Minecraft minecraft, EntityPlayer player, ScaledResolution resolution, FontRenderer fontRendererObj, RenderGameOverlayEvent.ElementType type, ItemStack playersRarmorChestplate, ItemStack module, float partialTicks){
    }

    /**
     * @param world           The World of the Player
     * @param player          The Player itself
     * @param armorChestplate The Rarmor Chestplate
     * @param damageSource    The Type of Damage the Player take
     * @param damage          The Amount of Damage th Player take
     * @return true if you want to cancel the Damage
     */
    default boolean onPlayerTakeDamage(World world, EntityPlayer player, ItemStack armorChestplate, DamageSource damageSource, float damage){
        return false;
    }

    default void onContainerTick(ContainerBase container, EntityPlayer player, InventoryBase inventory, ItemStack armorChestplate, ItemStack module){
    }

    default void initModule(World world, EntityPlayer player, InventoryBase inventory, ItemStack armorChestplate, ItemStack module){
    }

    enum ModuleType{
        ACTIVE,
        PASSIVE,
        NONE
    }
}
