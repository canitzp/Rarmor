package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.ITabTickable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public abstract class RarmorOneSlotTab implements ITabTickable{

    private String tabName;
    private InventoryBasic inventory = new InventoryBasic(this.tabName + " Inventory", false, 1);
    private static final ResourceLocation guiLoc = new ResourceLocation(Rarmor.MODID, "");

    public RarmorOneSlotTab(String tabName){
        this.tabName = tabName;
    }

    @Override
    public String getTabIdentifier(ItemStack rarmor, EntityPlayer player){
        return Rarmor.MODID + ":" + this.tabName.toLowerCase() + "tab";
    }

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

    @Override
    public void drawGui(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){
        gui.mc.getTextureManager().bindTexture(guiLoc);
        gui.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 0, 0);
    }
}
