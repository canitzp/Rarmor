/*
 * This file 'ItemModuleEnergeticLoader.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items.rfarmor.modules;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.inventory.gui.GuiRFArmor;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.util.NBTUtil;
import de.canitzp.rarmor.util.SlotUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class ItemModuleEnergeticLoader extends ItemModule implements IRarmorModule{

    public ItemModuleEnergeticLoader(){
        super("moduleEnergeticLoader");
    }

    @Override
    public String getUniqueName(){
        return "EnergeticLoader";
    }

    @Override
    public String getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return "If you have some tools and they need some energy, this module simply uses the energy of your Rarmor to power up the tool.";
    }

    @Override
    public ModuleType getModuleType(){
        return ModuleType.ACTIVE;
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        RarmorUtil.toggleSlotInGui(140, 18, true);
        RarmorUtil.dropSlot(inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT), player);
        SlotUtil.clearSlot(inventory, ItemRFArmorBody.GENERATORSLOT);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, boolean isColoringTab, float partialTicks, int mouseX, int mouseY){
        if(!settingActivated && !isColoringTab){
            RarmorUtil.toggleSlotInGui(140, 18, false);
            minecraft.getTextureManager().bindTexture(((GuiRFArmor) gui).modulesGui);
            gui.drawTexturedModalRect(gui.guiLeft + 120, gui.guiTop + 13, 0, 73, 56, 55);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainer gui, ItemStack armor, ItemStack module, boolean settingActivated, boolean isColoringTab, int type, int mouseX, int mouseY){
        RarmorUtil.toggleSlotInGui(140, 18, settingActivated || isColoringTab);
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        ItemStack chargeSlot = inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT);
        if(chargeSlot != null && chargeSlot.getItem() instanceof IEnergyContainerItem){
            ((IEnergyContainerItem) chargeSlot.getItem()).receiveEnergy(chargeSlot, NBTUtil.getInteger(armorChestplate, "rfPerTick"), false);
            ((IEnergyContainerItem) armorChestplate.getItem()).extractEnergy(armorChestplate, NBTUtil.getInteger(armorChestplate, "rfPerTick"), false);
        }
    }
}
