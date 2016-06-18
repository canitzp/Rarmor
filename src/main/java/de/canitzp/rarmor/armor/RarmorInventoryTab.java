package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author canitzp
 */
public class RarmorInventoryTab implements IRarmorTab{

    private final ResourceLocation tabLoc = new ResourceLocation(Rarmor.MODID, "textures/gui/guiTabInventory.png");
    private InventoryBasic inventory;

    @Override
    public String getTabIdentifier(ItemStack rarmor, EntityPlayer player){
        return Rarmor.MODID + ":inventoryTab";
    }

    @Override
    public List<Slot> manipulateSlots(Container container, EntityPlayer player, List<Slot> slotList, int containerOffsetLeft, int containerOffsetTop){
        for (int j = 0; j < 7; ++j){
            for (int k = 0; k < 9; ++k){
                slotList.add(new Slot(inventory, k + j * 9, 5 + containerOffsetLeft + k * 18, 6 + containerOffsetTop + j * 18));
            }
        }
        return slotList;
    }

    @Override
    public boolean canBeVisible(IInventory inventory, ItemStack rarmor, EntityPlayer player){
        if(inventory != null){
            ItemStack stack = inventory.getStackInSlot(0);
            if(stack != null && stack.isItemEqual(new ItemStack(Blocks.CHEST))){
                return true;
            }
        }
        return false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        return NBTUtil.writeInventory(nbt, this.inventory);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        this.inventory = NBTUtil.readInventory(nbt, "Rarmor Inventory Tab", 63);
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
        gui.mc.getTextureManager().bindTexture(this.tabLoc);
        gui.drawTexturedModalRect(guiLeft + 4, guiTop + 4, 0, 0, 239, 138);
    }

}
