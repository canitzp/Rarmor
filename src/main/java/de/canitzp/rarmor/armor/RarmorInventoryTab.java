package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.GuiUtils;
import de.canitzp.rarmor.api.IRarmorTab;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author canitzp
 */
public class RarmorInventoryTab implements IRarmorTab{

    private InventoryBasic inventory = new InventoryBasic("Rarmor Inventory Tab", false, 63);

    @Override
    public String getTabIdentifier(ItemStack rarmor, EntityPlayer player){
        return Rarmor.MODID + ":inventoryTab";
    }

    @Override
    public List<Slot> manipulateSlots(Container container, EntityPlayer player, List<Slot> slotList, InventoryBasic energyField, int containerOffsetLeft, int containerOffsetTop){
        for (int j = 0; j < 7; ++j){
            for (int k = 0; k < 9; ++k){
                slotList.add(new Slot(inventory, k + j * 9, 5 + containerOffsetLeft + k * 18, 5 + containerOffsetTop + j * 18));
            }
        }
        return slotList;
    }

    @Override
    public boolean canBeVisible(ItemStack rarmor, EntityPlayer player){
        NBTTagCompound nbt = NBTUtil.getTagFromStack(rarmor);
        return nbt.hasKey("DependencyChest") && nbt.getBoolean("DependencyChest");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        return NBTUtil.writeInventory(nbt, this.inventory);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        this.inventory = NBTUtil.readInventory(nbt, this.inventory);
    }

    @Override
    public ItemStack getTabIcon(){
        return new ItemStack(Blocks.CHEST);
    }

    @Override
    public String getTabHoveringText(){
        return "Inventory";
    }

    @Override
    public void drawGui(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){
        GuiUtils.drawSlotField(gui, guiLeft + 8, guiTop + 8, 9, 7);
        GuiUtils.drawSlotField(gui, guiLeft + 180, guiTop + 19, 3, 3);
        GuiUtils.drawSlot(gui, guiLeft + 199, guiTop + 105);
        GuiUtils.drawCraftingArrow(gui, guiLeft + 200, guiTop + 78);
    }

}
