package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.IRarmorTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
public class RarmorColoringTab implements IRarmorTab{

    private final ResourceLocation tabLoc = new ResourceLocation(Rarmor.MODID, "textures/gui/guiTabColoring.png");
    private final ResourceLocation iconLoc = new ResourceLocation(Rarmor.MODID, "textures/gui/guiRarmor.png");
    private ArmorSlot clickedSlot = ArmorSlot.CHEST;

    @Override
    public List<Slot> manipulateSlots(Container container, EntityPlayer player, List<Slot> slotList, int containerOffsetLeft, int containerOffsetTop){
        for (int k = 0; k < 4; ++k) {
            final EntityEquipmentSlot entityequipmentslot = RarmorOverviewTab.VALID_EQUIPMENT_SLOTS[k];
            slotList.add(new Slot(player.inventory, 36 + (3 - k),containerOffsetLeft + 29, containerOffsetTop + 30 + k * 20) {
                @SideOnly(Side.CLIENT)
                public String getSlotTexture(){
                    return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
                }
                @Override
                public boolean canTakeStack(EntityPlayer playerIn){
                    return false;
                }
            });
        }
        return slotList;
    }

    @Override
    public String getTabIdentifier(ItemStack rarmor, EntityPlayer player){
        return Rarmor.MODID + ":coloringTab";
    }

    @Override
    public void drawTab(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop){
        gui.mc.getTextureManager().bindTexture(this.iconLoc);
        gui.drawTexturedModalRect(guiLeft + 1, guiTop, 91, 226, 16, 16);
    }

    @Override
    public String getTabHoveringText(){
        return "Rarmor Color";
    }

    @Override
    public void drawGui(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){
        gui.mc.getTextureManager().bindTexture(this.tabLoc);
        gui.drawTexturedModalRect(guiLeft + 28, guiTop + 16, 0, 0, 188, 112);
        if(this.clickedSlot != null){
            gui.drawTexturedModalRect(guiLeft + 28 + 5, guiTop + 16 + this.clickedSlot.yValue, 0, 138, 16, 16);
        }
    }

    @Override
    public void onMouseClick(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, int btnID){
        if(guiLeft + 32 <= mouseX && guiLeft + 48 >= mouseX){
            for(ArmorSlot armor : ArmorSlot.values()){
                if(guiTop + 16 + armor.yValue <= mouseY && guiTop + 16 + armor.yValue + 16 >= mouseY){
                    this.clickedSlot = armor;
                }
            }
        }
    }

    enum ArmorSlot{
        HEAD(18),
        CHEST(38),
        LEGGINS(58),
        BOOTS(78);
        public int yValue;
        ArmorSlot(int yValue){
            this.yValue = yValue;
        }
    }
}
