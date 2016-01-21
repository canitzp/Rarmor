package de.canitzp.rarmor.items;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.inventory.container.Slots.SlotModule;
import de.canitzp.rarmor.inventory.gui.GuiRFArmor;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.util.util.ItemStackUtil;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

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
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory) {
        if (isGenerator(inventory, armorChestplate) || NBTUtil.getInteger(module, "GenBurnTime") > 0) {
            this.generate(armorChestplate, module);
        } else {
            NBTUtil.setInteger(module, "GenBurnTime", 0);
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory) {
        dropSlot(inventory, inventory.getStackInSlot(30), player, armorChestplate);
        NBTUtil.setInteger(module, "GenBurnTime", 0);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY) {
        if (!settingActivated) {
            int i = 0;
            if (NBTUtil.getInteger(module, "CurrentItemGenBurnTime") != 0) {
                i = (NBTUtil.getInteger(module, "CurrentItemGenBurnTime") - NBTUtil.getInteger(module, "GenBurnTime")) * 13 / NBTUtil.getInteger(module, "CurrentItemGenBurnTime");
            }
            minecraft.getTextureManager().bindTexture(((GuiRFArmor) gui).modulesGui);
            gui.drawTexturedModalRect(((GuiRFArmor) gui).getGuiLeft() + 120, ((GuiRFArmor) gui).getGuiTop() + 13, 57, 0, 56, 55);
            if (NBTUtil.getInteger(module, "GenBurnTime") > 0) {
                gui.drawTexturedModalRect(((GuiRFArmor) gui).getGuiLeft() + 141, ((GuiRFArmor) gui).getGuiTop() + 39 + 12 - i, 58, 57 + 12 - i, 14, i + 1);
            }
        }
    }

    @Override
    public void drawScreen(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY) {}

    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY) {}

    @Override
    public boolean showSlot(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, Slot slot, int mouseX, int mouseY, int slotX, int slotY, boolean isMouseOverSlot) {
       //return !(settingActivated && slot instanceof SlotModule) && isMouseOverSlot;
        Slot moduleSlot = this.getSlotAtPosition(gui, 15, 34);
        if(moduleSlot != null){
            if(moduleSlot.getStack() != null && !settingActivated){
                Item item = moduleSlot.getStack().getItem();
                if(item instanceof ItemModuleGenerator){
                    return isMouseOverSlot;
                }
            }
            return isMouseOverSlot && !(slot instanceof SlotModule);
        }
        return isMouseOverSlot;
    }

    private boolean isGenerator(IInventory inv, ItemStack armor){
        if(armor.getItem() instanceof IEnergyContainerItem){
            int burnTime = TileEntityFurnace.getItemBurnTime(inv.getStackInSlot(ItemRFArmorBody.GENERATORSLOT));
            int energyToProduce = burnTime * NBTUtil.getInteger(armor, "rfPerTick");
            return burnTime > 0 && NBTUtil.getInteger(armor, "Energy") + (energyToProduce / 4) <= ((IEnergyContainerItem) armor.getItem()).getMaxEnergyStored(armor);
        }
        return false;
    }

    private void generate(ItemStack armor, ItemStack module){
        IInventory inventory = NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount);
        int burnTime = NBTUtil.getInteger(module, "GenBurnTime");
        if(burnTime == 0){
            NBTUtil.setInteger(module, "CurrentItemGenBurnTime", TileEntityFurnace.getItemBurnTime(inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT)));
            ItemStack burnItem = inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT);
            inventory = ItemStackUtil.reduceStackSize(inventory, ItemRFArmorBody.GENERATORSLOT);
            if(burnItem.getItem().getContainerItem() != null) {
                inventory = ItemStackUtil.addStackToSlot(inventory, new ItemStack(burnItem.getItem().getContainerItem()), ItemRFArmorBody.GENERATORSLOT);
            }
            NBTUtil.saveSlots(armor, inventory);
        }
        if(burnTime < NBTUtil.getInteger(module, "CurrentItemGenBurnTime")){
            burnTime++;
            ((IEnergyContainerItem)armor.getItem()).receiveEnergy(armor, NBTUtil.getInteger(armor, "rfPerTick"), false);
        }else {
            burnTime = 0;
            NBTUtil.setInteger(module, "CurrentItemGenBurnTime", 0);
        }
        NBTUtil.setInteger(module, "GenBurnTime", burnTime);
    }

    private void dropSlot(IInventory inventory, ItemStack stack, EntityPlayer player, ItemStack armor) {
        if(stack != null){
            if(!player.worldObj.isRemote){
                player.dropPlayerItemWithRandomChoice(stack, false);
            }
            inventory.setInventorySlotContents(30, null);
            NBTUtil.saveSlots(armor, inventory);
        }
    }

    private Slot getSlotAtPosition(GuiContainer gui, int x, int y) {
        for (int k = 0; k < gui.inventorySlots.inventorySlots.size(); ++k) {
            Slot slot = gui.inventorySlots.inventorySlots.get(k);
            if(x >= slot.xDisplayPosition - 1 && x < slot.xDisplayPosition + 16 + 1 && slot.yDisplayPosition >= y - 1 && slot.yDisplayPosition < y + 16 + 1){
                return slot;
            }
        }
        return null;
    }

}
