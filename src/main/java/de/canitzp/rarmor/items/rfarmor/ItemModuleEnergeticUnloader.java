package de.canitzp.rarmor.items.rfarmor;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.gui.GuiContainerBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import de.canitzp.rarmor.inventory.gui.GuiRFArmor;
import de.canitzp.rarmor.util.JavaUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
public class ItemModuleEnergeticUnloader extends ItemModule implements IRarmorModule {

    public ItemModuleEnergeticUnloader() {
        super("moduleEnergeticUnloader");
    }

    @Override
    public String getUniqueName() {
        return "EnergeticUnloader";
    }

    @Override
    public List<String> getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips) {
        return JavaUtil.newList("If you have some Tools and your Rarmor needs more Energy,",
                "simply use the Energy of them to Power up your Rarmor.");
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.ACTIVE;
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, SlotUpdate slot) {
        RarmorUtil.toggleSlotInGui(140, 18, true);
        RarmorUtil.dropSlot(inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT), player);
        slot.inv.setInventorySlotContents(30, null);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY, int guiLeft, int guiTop) {
        if (!settingActivated) {
            RarmorUtil.toggleSlotInGui(140, 18, false);
            minecraft.getTextureManager().bindTexture(((GuiRFArmor) gui).modulesGui);
            gui.drawTexturedModalRect(gui.getGuiLeft() + 120, gui.getGuiTop() + 13, 0, 73, 56, 55);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainerBase gui, ItemStack armor, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY, int guiLeft, int guiTop) {
        RarmorUtil.toggleSlotInGui(140, 18, settingActivated);
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory) {
        ItemStack chargeSlot = inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT);
        if (chargeSlot != null && chargeSlot.getItem() instanceof IEnergyContainerItem) {
            ((IEnergyContainerItem) chargeSlot.getItem()).extractEnergy(chargeSlot, NBTUtil.getInteger(armorChestplate, "rfPerTick"), false);
            ((IEnergyContainerItem) armorChestplate.getItem()).receiveEnergy(armorChestplate, NBTUtil.getInteger(armorChestplate, "rfPerTick"), false);
        }
    }
}
