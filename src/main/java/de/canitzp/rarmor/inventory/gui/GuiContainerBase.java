package de.canitzp.rarmor.inventory.gui;

import de.canitzp.rarmor.api.ISpecialSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

/**
 * @author canitzp
 */
public abstract class GuiContainerBase extends GuiContainer {

    private ItemStack draggedStack;
    private boolean isRightMouseClick;
    private int dragSplittingRemnant;
    private ItemStack returningStack;
    private long returningStackTime;
    private Slot returningStackDestSlot;
    private int touchUpX;
    private int touchUpY;
    private Slot clickedSlot;
    private int dragSplittingLimit;

    public GuiContainerBase(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Override
    protected abstract void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int i = this.guiLeft;
        int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        //super.drawScreen(mouseX, mouseY, partialTicks);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)i, (float)j, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        Slot theSlot = null;
        int k = 240;
        int l = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
            Slot slot = this.inventorySlots.inventorySlots.get(i1);
            this.drawSlot(slot);
            if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.canBeHovered()) {
                theSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                int j1 = slot.xDisplayPosition;
                int k1 = slot.yDisplayPosition;
                GlStateManager.colorMask(true, true, true, false);
                this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableGUIStandardItemLighting();
        InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
        ItemStack itemstack = this.draggedStack == null ? inventoryplayer.getItemStack() : this.draggedStack;
        if (itemstack != null) {
            int j2 = 8;
            int k2 = this.draggedStack == null ? 8 : 16;
            String s = null;
            if (this.draggedStack != null && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.stackSize = MathHelper.ceiling_float_int((float)itemstack.stackSize / 2.0F);
            }
            else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.stackSize = this.dragSplittingRemnant;
                if (itemstack.stackSize == 0) {
                    s = "" + TextFormatting.YELLOW + "0";
                }
            }
            this.drawItemStack(itemstack, mouseX - i - j2, mouseY - j - k2, s);
        }
        if (this.returningStack != null) {
            float f = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;
            if (f >= 1.0F) {
                f = 1.0F;
                this.returningStack = null;
            }
            int l2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
            int i3 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
            int l1 = this.touchUpX + (int)((float)l2 * f);
            int i2 = this.touchUpY + (int)((float)i3 * f);
            this.drawItemStack(this.returningStack, l1, i2, null);
        }
        GlStateManager.popMatrix();
        if (inventoryplayer.getItemStack() == null && theSlot != null && theSlot.getHasStack()) {
            ItemStack itemstack1 = theSlot.getStack();
            this.renderToolTip(itemstack1, mouseX, mouseY);
        }
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
    }

    private void drawItemStack(ItemStack stack, int x, int y, String altText) {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = null;
        if (stack != null) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRendererObj;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack == null ? 0 : 8), altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
    }

    private void drawSlot(Slot slotIn) {
        if (slotIn instanceof ISpecialSlot) {
            if (!((ISpecialSlot) slotIn).doesSlotExist()) {
                return;
            }
        }
        int i = slotIn.xDisplayPosition;
        int j = slotIn.yDisplayPosition;
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag1 = slotIn == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
        ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
        String s = null;
        if (slotIn == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null) {
            itemstack = itemstack.copy();
            itemstack.stackSize /= 2;
        } else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && itemstack1 != null) {
            if (this.dragSplittingSlots.size() == 1) {
                return;
            }
            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack() == null ? 0 : slotIn.getStack().stackSize);
                if (itemstack.stackSize > itemstack.getMaxStackSize()) {
                    s = TextFormatting.YELLOW + "" + itemstack.getMaxStackSize();
                    itemstack.stackSize = itemstack.getMaxStackSize();
                }
                if (itemstack.stackSize > slotIn.getItemStackLimit(itemstack)) {
                    s = TextFormatting.YELLOW + "" + slotIn.getItemStackLimit(itemstack);
                    itemstack.stackSize = slotIn.getItemStackLimit(itemstack);
                }
            } else {
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }
        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;
        if (itemstack == null) {
            TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();
            if (textureatlassprite != null) {
                GlStateManager.disableLighting();
                this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
                this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                GlStateManager.enableLighting();
                flag1 = true;
            }
        }
        if (!flag1) {
            if (flag) {
                drawRect(i, j, i + 16, j + 16, -2130706433);
            }
            if (itemstack != null) {
                if(slotIn instanceof ISpecialSlot) {
                    if(((ISpecialSlot) slotIn).doesSlotExist()){
                        GlStateManager.enableDepth();
                        this.itemRender.renderItemAndEffectIntoGUI(itemstack, i, j);
                        this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
                    }
                } else {
                    GlStateManager.enableDepth();
                    this.itemRender.renderItemAndEffectIntoGUI(itemstack, i, j);
                    this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
                }
            }
        }
        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    private void updateDragSplitting() {
        ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();
        if (itemstack != null && this.dragSplitting) {
            this.dragSplittingRemnant = itemstack.stackSize;
            for (Slot slot : this.dragSplittingSlots) {
                ItemStack itemstack1 = itemstack.copy();
                int i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
                if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
                    itemstack1.stackSize = itemstack1.getMaxStackSize();
                }
                if (itemstack1.stackSize > slot.getItemStackLimit(itemstack1)) {
                    itemstack1.stackSize = slot.getItemStackLimit(itemstack1);
                }
                this.dragSplittingRemnant -= itemstack1.stackSize - i;
            }
        }
    }

    private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
        return this.isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
    }

}
