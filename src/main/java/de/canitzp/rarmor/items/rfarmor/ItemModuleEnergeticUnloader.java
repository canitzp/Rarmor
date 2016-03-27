package de.canitzp.rarmor.items.rfarmor;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.inventory.gui.GuiRFArmor;
import de.canitzp.rarmor.inventory.slots.SlotModule;
import de.canitzp.rarmor.util.NBTUtil;
import de.canitzp.rarmor.util.SlotUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory, Slot slot) {
        RarmorUtil.dropSlot(inventory, inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT), player, armorChestplate);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY, int guiLeft, int guiTop) {
        if (!settingActivated) {
            minecraft.getTextureManager().bindTexture(((GuiRFArmor) gui).modulesGui);
            gui.drawTexturedModalRect(((GuiRFArmor) gui).getGuiLeft() + 120, ((GuiRFArmor) gui).getGuiTop() + 13, 0, 73, 56, 55);
        }
    }

    @Override
    public boolean showSlot(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, Slot slot, int mouseX, int mouseY, int slotX, int slotY, boolean isMouseOverSlot) {
        return !(settingActivated && slot instanceof SlotModule) && isMouseOverSlot;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainer gui, ItemStack armor, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY, int guiLeft, int guiTop) {
        Slot moduleSlot = SlotUtil.getSlotAtPosition(gui, 140, 18);
        if (moduleSlot != null) {
            if (moduleSlot instanceof SlotModule) {
                if (settingActivated) {
                    ((SlotModule) moduleSlot).setSlotInactive();
                }
                if (!settingActivated) {
                    ((SlotModule) moduleSlot).setSlotActive();
                }
            }

        }
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory) {
        ItemStack chargeSlot = inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT);
        if(chargeSlot != null && chargeSlot.getItem() instanceof IEnergyContainerItem){
            ((IEnergyContainerItem) chargeSlot.getItem()).extractEnergy(chargeSlot, NBTUtil.getInteger(armorChestplate, "rfPerTick"), false);
            ((IEnergyContainerItem) armorChestplate.getItem()).receiveEnergy(armorChestplate, NBTUtil.getInteger(armorChestplate, "rfPerTick"), false);
        }
    }
}
