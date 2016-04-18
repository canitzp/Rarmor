package de.canitzp.rarmor.items.rfarmor;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.gui.GuiContainerBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import de.canitzp.rarmor.inventory.gui.GuiRFArmor;
import de.canitzp.rarmor.util.ItemStackUtil;
import de.canitzp.rarmor.util.JavaUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

/**
 * @author canitzp
 */
public class ItemModuleGenerator extends ItemModule implements IRarmorModule {

    public ItemModuleGenerator() {
        super("moduleGenerator");
    }

    @Override
    public String getUniqueName() {
        return "Generator";
    }

    @Override
    public List<String> getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips) {
        return JavaUtil.newList("The Generator Module produce some Energy for the Rarmor.", "It produce " + TextFormatting.RED + ItemRFArmorBody.rfPerTick + TextFormatting.GRAY + "RF per Tick.",
                "One Coal adds " + TextFormatting.RED + TileEntityFurnace.getItemBurnTime(new ItemStack(Items.COAL)) * ItemRFArmorBody.rfPerTick + TextFormatting.GRAY + "RF to the Rarmor.");
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.ACTIVE;
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        NBTUtil.setInteger(stack, "GenBurnTime", 0);
        NBTUtil.setInteger(stack, "CurrentItemGenBurnTime", 0);
        NBTUtil.setInteger(stack, "rfPerTick", ItemRFArmorBody.rfPerTick);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack stack = new ItemStack(item);
        NBTUtil.setInteger(stack, "GenBurnTime", 0);
        NBTUtil.setInteger(stack, "CurrentItemGenBurnTime", 0);
        NBTUtil.setInteger(stack, "rfPerTick", ItemRFArmorBody.rfPerTick);
        list.add(stack);
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory) {
        if (isGenerator(inventory, armorChestplate) || NBTUtil.getInteger(module, "GenBurnTime") > 0) {
            this.generate(armorChestplate, module);
        } else {
            NBTUtil.setInteger(module, "GenBurnTime", 0);
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, SlotUpdate slot) {
        RarmorUtil.toggleSlotInGui(140, 18, true);
        dropSlot(inventory, inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT), player, armorChestplate);
        NBTUtil.setInteger(module, "GenBurnTime", 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainerBase gui, ItemStack armor, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY, int guiLeft, int guiTop) {
        if (!settingActivated) {
            RarmorUtil.toggleSlotInGui(140, 18, false);
            int i = 0;
            if (NBTUtil.getInteger(module, "CurrentItemGenBurnTime") != 0) {
                i = (NBTUtil.getInteger(module, "CurrentItemGenBurnTime") - NBTUtil.getInteger(module, "GenBurnTime")) * 13 / NBTUtil.getInteger(module, "CurrentItemGenBurnTime");
            }
            minecraft.getTextureManager().bindTexture(((GuiRFArmor) gui).modulesGui);
            gui.drawTexturedModalRect(gui.getGuiLeft() + 120, gui.getGuiTop() + 13, 57, 0, 56, 55);
            if (NBTUtil.getInteger(module, "GenBurnTime") > 0) {
                gui.drawTexturedModalRect(gui.getGuiLeft() + 141, gui.getGuiTop() + 39 + 12 - i, 58, 57 + 12 - i, 14, i + 1);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainerBase gui, ItemStack armor, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY, int guiLeft, int guiTop) {
        RarmorUtil.toggleSlotInGui(140, 18, settingActivated);
    }

    private boolean isGenerator(IInventory inv, ItemStack armor) {
        if (armor.getItem() instanceof IEnergyContainerItem) {
            int burnTime = TileEntityFurnace.getItemBurnTime(inv.getStackInSlot(ItemRFArmorBody.GENERATORSLOT));
            int energyToProduce = burnTime * NBTUtil.getInteger(armor, "rfPerTick");
            return burnTime > 0 && NBTUtil.getInteger(armor, "Energy") + (energyToProduce / 4) <= ((IEnergyContainerItem) armor.getItem()).getMaxEnergyStored(armor);
        }
        return false;
    }

    private void generate(ItemStack armor, ItemStack module) {
        InventoryBase inventory = RarmorUtil.readRarmor(armor);
        int burnTime = NBTUtil.getInteger(module, "GenBurnTime");
        if (burnTime == 0) {
            NBTUtil.setInteger(module, "CurrentItemGenBurnTime", TileEntityFurnace.getItemBurnTime(inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT)));
            ItemStack burnItem = inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT);
            inventory = ItemStackUtil.reduceStackSize(inventory, ItemRFArmorBody.GENERATORSLOT);
            System.out.println(Arrays.toString(inventory.slots));
            if (burnItem.getItem().getContainerItem() != null) {
                inventory = ItemStackUtil.addStackToSlot(inventory, new ItemStack(burnItem.getItem().getContainerItem()), ItemRFArmorBody.GENERATORSLOT);
            }
            RarmorUtil.saveRarmor(armor, inventory);
        }
        if (burnTime < NBTUtil.getInteger(module, "CurrentItemGenBurnTime")) {
            burnTime++;
            ((IEnergyContainerItem) armor.getItem()).receiveEnergy(armor, NBTUtil.getInteger(armor, "rfPerTick"), false);
        } else {
            burnTime = 0;
            NBTUtil.setInteger(module, "CurrentItemGenBurnTime", 0);
        }
        NBTUtil.setInteger(module, "GenBurnTime", burnTime);
    }

    private void dropSlot(InventoryBase inventory, ItemStack stack, EntityPlayer player, ItemStack armor) {
        if (stack != null) {
            if (!player.worldObj.isRemote) {
                player.dropItem(stack, false);
            }
            inventory.setInventorySlotContents(30, null);
            RarmorUtil.saveRarmor(armor, inventory);
        }
    }

    private Slot getSlotAtPosition(GuiContainer gui, int x, int y) {
        for (int k = 0; k < gui.inventorySlots.inventorySlots.size(); ++k) {
            Slot slot = gui.inventorySlots.inventorySlots.get(k);
            if (x >= slot.xDisplayPosition - 1 && x < slot.xDisplayPosition + 16 + 1 && slot.yDisplayPosition >= y - 1 && slot.yDisplayPosition < y + 16 + 1) {
                return slot;
            }
        }
        return null;
    }

}
