package de.canitzp.rarmor.items;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.util.util.EnergyUtil;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemModuleSolarPanel extends ItemModule implements IRarmorModule {

    public ItemModuleSolarPanel() {
        super("moduleSolarPanel");
    }

    @Override
    public String getUniqueName() {
        return "SolarPanel";
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory) {
        if(canPlayerSeeSky(player)){
            EnergyUtil.addEnergy(armorChestplate, NBTUtil.getInteger(armorChestplate, "rfPerTick") / 5, armorChestplate.getMaxDamage());
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory) {}

    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY) {}

    @Override
    public void drawScreen(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY) {}

    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY) {}

    @Override
    public boolean showSlot(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, Slot slot, int mouseX, int mouseY, boolean isMouseOverSlot) {
        return isMouseOverSlot;
    }

    private boolean canPlayerSeeSky(EntityPlayer player) {
        int i = (int)player.posX & 15;
        int j = (int)player.posY;
        int k = (int)player.posZ & 15;
        int[] heightMap = new int[256];
        return j >= heightMap[k << 4 | i];
    }

}
