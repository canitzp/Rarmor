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
import de.canitzp.rarmor.util.SlotUtil;
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
public class ItemModuleEnergeticLoader extends ItemModule implements IRarmorModule{

    public ItemModuleEnergeticLoader(){
        super("moduleEnergeticLoader");
    }

    @Override
    public String getUniqueName(){
        return "EnergeticLoader";
    }

    @Override
    public List<String> getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return JavaUtil.newList("If you have some Tools and they need some Energy,",
                "simply use the Energy of your Rarmor to Power up the Tool.");
    }

    @Override
    public ModuleType getModuleType(){
        return ModuleType.ACTIVE;
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, SlotUpdate slot){
        RarmorUtil.toggleSlotInGui(140, 18, true);
        RarmorUtil.dropSlot(inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT), player);
        SlotUtil.clearSlot(slot.inv, ItemRFArmorBody.GENERATORSLOT);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY, int guiLeft, int guiTop){
        if (!settingActivated){
            RarmorUtil.toggleSlotInGui(140, 18, false);
            minecraft.getTextureManager().bindTexture(((GuiRFArmor) gui).modulesGui);
            gui.drawTexturedModalRect(gui.getGuiLeft() + 120, gui.getGuiTop() + 13, 0, 73, 56, 55);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainerBase gui, ItemStack armor, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY, int guiLeft, int guiTop){
        RarmorUtil.toggleSlotInGui(140, 18, settingActivated);
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        ItemStack chargeSlot = inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT);
        if (chargeSlot != null && chargeSlot.getItem() instanceof IEnergyContainerItem){
            ((IEnergyContainerItem) chargeSlot.getItem()).receiveEnergy(chargeSlot, NBTUtil.getInteger(armorChestplate, "rfPerTick"), false);
            ((IEnergyContainerItem) armorChestplate.getItem()).extractEnergy(armorChestplate, NBTUtil.getInteger(armorChestplate, "rfPerTick"), false);
        }
    }
}
