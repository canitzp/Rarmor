package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.network.PacketPaintRarmor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public class RarmorColoringTab implements IRarmorTab{

    private final ResourceLocation tabLoc = new ResourceLocation(Rarmor.MODID, "textures/gui/guiTabColoring.png");
    private ArmorSlot clickedSlot = ArmorSlot.CHEST;
    private List<ColorButton> availabeColors = new ArrayList<>();

    public RarmorColoringTab(){
        int x = 0, y = 0;
        for(Map.Entry<Integer, String> entry : RarmorAPI.registerColor.entrySet()){
            availabeColors.add(new ColorButton(new Color(entry.getKey(), entry.getValue()), x * 10 + 29 + 28, y * 10 + 17 + 16));
            x++;
            if(x >= 16){
                y++;
                x = 0;
            }
        }
    }

    @Override
    public List<Slot> manipulateSlots(Container container, EntityPlayer player, List<Slot> slotList, InventoryBasic energyField, int containerOffsetLeft, int containerOffsetTop){
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
        gui.mc.getTextureManager().bindTexture(this.tabLoc);
        gui.drawTexturedModalRect(guiLeft, guiTop, 24, 138, 16, 16);
    }

    @Override
    public String getTabHoveringText(){
        return "Rarmor Color";
    }

    @Override
    public void drawGui(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){
        gui.mc.getTextureManager().bindTexture(this.tabLoc);
        gui.drawTexturedModalRect(guiLeft + 28, guiTop + 16, 0, 0, 191, 112);
        if(this.clickedSlot != null){
            gui.drawTexturedModalRect(guiLeft + 28 + 5, guiTop + 16 + this.clickedSlot.yValue, 0, 138, 16, 16);
        }
        for(ColorButton button : this.availabeColors){
            button.render(gui, guiLeft, guiTop);
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
        for(ColorButton button : this.availabeColors){
            Color color = button.onClick(guiLeft, guiTop, mouseX, mouseY);
            if(color != null && this.clickedSlot != null){
                ItemStack part = RarmorUtil.getPlayerArmorPart(player, this.clickedSlot.slot);
                if(part != null){
                    NBTUtil.setColor(part, color);
                    Rarmor.proxy.network.sendToServer(new PacketPaintRarmor(player, this.clickedSlot.slot, color));
                }
            }
        }
    }

    @Override
    public void drawForeground(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){
        for(ColorButton button : this.availabeColors){
            button.drawScreen((GuiContainerRarmor.GuiRarmor) gui, guiLeft, guiTop, mouseX, mouseY);
        }
    }

    enum ArmorSlot{
        HEAD(18, EntityEquipmentSlot.HEAD),
        CHEST(38, EntityEquipmentSlot.CHEST),
        LEGGINS(58, EntityEquipmentSlot.LEGS),
        BOOTS(78, EntityEquipmentSlot.FEET);
        public int yValue;
        public EntityEquipmentSlot slot;
        ArmorSlot(int yValue, EntityEquipmentSlot slot){
            this.yValue = yValue;
            this.slot = slot;
        }
    }

    public static class ColorButton extends Gui{
        public Color color;
        public int x, y, width = 8, height = 8;
        private ResourceLocation iconLoc = new ResourceLocation(Rarmor.MODID, "textures/gui/guiTabColoring.png");
        public ColorButton(Color color, int x, int y){
            this.color = color;
            this.x = x;
            this.y = y;
        }
        public void render(GuiContainer gui, int guiLeft, int guiTop){
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int i = this.color.hexValue;
            float f = (float)(i >> 16 & 255) / 255.0F;
            float f1 = (float)(i >> 8 & 255) / 255.0F;
            float f2 = (float)(i & 255) / 255.0F;
            GlStateManager.color(1.0F * f, 1.0F * f1, 1.0F * f2, 1.0F);
            gui.mc.getTextureManager().bindTexture(iconLoc);
            this.drawTexturedModalRect(this.x + guiLeft, this.y + guiTop, 16, 138, 8, 8);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
        public void drawScreen(GuiContainerRarmor.GuiRarmor gui, int guiLeft, int guiTop, int mouseX, int mouseY){
            if(isMouseOver(guiLeft, guiTop, mouseX, mouseY)){
                gui.drawHoveringText(Collections.singletonList(this.color.showName), mouseX, mouseY);
            }
        }

        public boolean isMouseOver(int guiLeft, int guiTop, int mouseX, int mouseY){
            if(mouseX >= x + guiLeft && mouseY >= y + guiTop) {
                if (mouseX <= x + width + guiLeft && mouseY <= y + height + guiTop) {
                    return true;
                }
            }
            return false;
        }

        public Color onClick(int guiLeft, int guiTop, int mouseX, int mouseY){
            if(isMouseOver(guiLeft, guiTop, mouseX, mouseY)) {
                return this.color;
            }
            return null;
        }
    }

    public static class Color{
        public String name, showName;
        public int hexValue;
        public Color(int hexValue, String name){
            this.hexValue = hexValue;
            this.name = name;
            this.showName = name + ": #" + hexValue;
        }
    }
}
