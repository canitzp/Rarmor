package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.GuiUtils;
import de.canitzp.rarmor.api.IRarmorTab;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
public abstract class RarmorOneSlotTab implements IRarmorTab{

    protected String tabName;
    protected InventoryBasic inventory;

    public RarmorOneSlotTab(String tabName){
        this.tabName = tabName;
        this.inventory = new InventoryBasic(tabName + "tab", false, 1);
    }

    @Override
    public String getTabIdentifier(ItemStack rarmor, EntityPlayer player){
        return Rarmor.MODID + ":" + this.tabName.replace(" ", "").toLowerCase() + "tab";
    }

    @Override
    public List<Slot> manipulateSlots(Container container, EntityPlayer player, List<Slot> slotList, InventoryBasic energyField, int containerOffsetLeft, int containerOffsetTop){
        slotList.add(new Slot(inventory, 0, containerOffsetLeft + 111, containerOffsetTop + 56));
        return slotList;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getTabHoveringText(){
        return this.tabName;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        this.inventory = NBTUtil.readInventory(nbt, this.inventory);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        return NBTUtil.writeInventory(nbt, this.inventory);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawGui(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){
        GuiUtils.drawBigSlot(gui, guiLeft + 110, guiTop + 55);
    }
}
