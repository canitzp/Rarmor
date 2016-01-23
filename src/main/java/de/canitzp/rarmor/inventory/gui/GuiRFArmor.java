package de.canitzp.rarmor.inventory.gui;

import com.google.common.collect.Lists;
import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.inventory.container.Slots.ISpecialSlot;
import de.canitzp.rarmor.inventory.container.Slots.SlotCraftingInput;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketSendNBTBoolean;
import de.canitzp.util.gui.GuiCheckBox;
import de.canitzp.util.util.GuiUtil;
import de.canitzp.util.util.NBTUtil;
import de.canitzp.util.util.PacketUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.inventory.container.ContainerRFArmor;
import de.canitzp.rarmor.inventory.container.Slots.SlotModule;
import de.canitzp.rarmor.items.ItemModuleGenerator;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmor;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

/**
 * @author canitzp
 */
public class GuiRFArmor extends GuiContainerBase {

    private EntityPlayer player;
    private ItemStack armor;
    private ResourceLocation normalGui = new ResourceLocation(Rarmor.MODID, "textures/gui/guiRFArmorNormal.png");
    public ResourceLocation modulesGui = new ResourceLocation(Rarmor.MODID, "textures/gui/guiRFArmorModules.png");
    private float xSizeFloat;
    private float ySizeFloat;
    private List<GuiCheckBox> checkBoxList = Lists.newArrayList();
    public boolean isSettingsTab;

    public GuiRFArmor(EntityPlayer player, ContainerRFArmor containerRFArmor) {
        super(containerRFArmor);
        this.xSize = 247;
        this.ySize = 226;
        this.armor = player.getCurrentArmor(ItemRFArmor.ArmorType.BODY.getId() + 1);
        this.player = player;
    }

    @Override
    public void initGui() {
        super.initGui();
        GuiCheckBox setInWorldTooltip = new GuiCheckBox(this, new ResourceLocation(Rarmor.MODID, "textures/gui/checkBox.png"), 117, 12, 124, 10, "Show In-World Tooltips", Lists.newArrayList("Show Tooltips like", "the Amount of Fluid in Tanks"));
        setInWorldTooltip.setClicked(NBTUtil.getBoolean(armor, "SettingInWorldTooltip"));
        checkBoxList.add(setInWorldTooltip);
        ISpecialSlot c1 = (ISpecialSlot) this.getSlotAtPosition(180, 14);
        ISpecialSlot c2 = (ISpecialSlot) this.getSlotAtPosition(198, 14);
        ISpecialSlot c3 = (ISpecialSlot) this.getSlotAtPosition(216, 14);
        ISpecialSlot c4 = (ISpecialSlot) this.getSlotAtPosition(180, 32);
        ISpecialSlot c5 = (ISpecialSlot) this.getSlotAtPosition(198, 32);
        ISpecialSlot c6 = (ISpecialSlot) this.getSlotAtPosition(216, 32);
        ISpecialSlot c7 = (ISpecialSlot) this.getSlotAtPosition(180, 50);
        ISpecialSlot c8 = (ISpecialSlot) this.getSlotAtPosition(198, 50);
        ISpecialSlot c9 = (ISpecialSlot) this.getSlotAtPosition(216, 50);
        ISpecialSlot cO = (ISpecialSlot) this.getSlotAtPosition(217, 99);
        c1.setSlotExist(true);
        c2.setSlotExist(true);
        c3.setSlotExist(true);
        c4.setSlotExist(true);
        c5.setSlotExist(true);
        c6.setSlotExist(true);
        c7.setSlotExist(true);
        c8.setSlotExist(true);
        c9.setSlotExist(true);
        cO.setSlotExist(true);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(normalGui);
        //Draw Gui:
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        ItemRFArmorBody body = (ItemRFArmorBody) armor.getItem();
        int energy = body.getEnergyStored(armor);
        int factorOfBurnTime = (int) (NBTUtil.getInteger(armor, "BurnTime") * 11.9 / 200);
        //Draw Batterie:
        GuiUtil.drawBarHorizontal(normalGui, normalGui, this.guiLeft + 18, this.guiTop + 29, 55, 247, 10, 21, body.maxEnergy, energy);
        //Draw Furnace Burn Time:
        this.drawTexturedModalRect(this.guiLeft + 15, this.guiTop + 89 - factorOfBurnTime, 39, 237 - factorOfBurnTime, 16, factorOfBurnTime);


        //Draw Module things:
        ItemStack module = NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if(module != null){
            if(module.getItem() instanceof IRarmorModule){
                ((IRarmorModule) module.getItem()).drawGuiContainerBackgroundLayer(mc, this, module, this.isSettingsTab, par1, par2, par3);
            }
        }

        if(this.isSettingsTab){
            this.mc.getTextureManager().bindTexture(modulesGui);
            this.drawTexturedModalRect(this.guiLeft + 115, this.guiTop + 3, 114, 0, 129, 130);
            for(GuiCheckBox checkBox : checkBoxList){
                checkBox.drawCheckBox(this.guiLeft, this.guiTop);
            }
        }

        GuiInventory.drawEntityOnScreen(this.guiLeft + 88, this.guiTop + 74, 30, (float)(this.guiLeft + 88) - this.xSizeFloat, (float)(this.guiTop + 72 - 50) - this.ySizeFloat, this.mc.thePlayer);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
        this.xSizeFloat = (float)mouseX;
        this.ySizeFloat = (float)mouseY;
        super.drawScreen(mouseX, mouseY, renderPartialTicks);

        if(mouseX >= this.guiLeft + 18 && mouseY >= this.guiTop + 8 && mouseX <= this.guiLeft + 27 &&mouseY <= this.guiTop + 28){
            int energy = ((ItemRFArmorBody) armor.getItem()).getEnergyStored(armor);
            int cap = ((ItemRFArmorBody) armor.getItem()).getMaxEnergyStored(armor);
            this.drawHoveringText(Lists.newArrayList(Integer.toString(energy) + "/" + Integer.toString(cap) + " RF"), mouseX, mouseY, this.fontRendererObj);
        }
        if(mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 140 && mouseX <= this.guiLeft + 35 &&mouseY <= this.guiTop + 161) {
            this.drawHoveringText(Lists.newArrayList("Back to normal Inventory"), mouseX, mouseY, this.fontRendererObj);
        }
        if(mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 166 && mouseX <= this.guiLeft + 35 &&mouseY <= this.guiTop + 187) {
            this.drawHoveringText(Lists.newArrayList("Settings"), mouseX, mouseY, this.fontRendererObj);
        }


        //Draw Module things:
        ItemStack module = NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if(module != null){
            if(module.getItem() instanceof IRarmorModule){
                ((IRarmorModule) module.getItem()).drawScreen(mc, this, module, this.isSettingsTab, renderPartialTicks, mouseX, mouseY);
            }
        }

        if(this.isSettingsTab){
            for(GuiCheckBox checkBox : checkBoxList){
                checkBox.mouseOverEvent(mouseX, mouseY, this.guiLeft, this.guiTop, fontRendererObj);
            }
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int type){
        if(type == 0){
            if(mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 140 && mouseX <= this.guiLeft + 35 && mouseY <= this.guiTop + 161) {
                this.armor.getTagCompound().setBoolean("click", true);
                PacketUtil.openPlayerInventoryFromClient(this.mc, this);
            }
            if(mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 166 && mouseX <= this.guiLeft + 35 &&mouseY <= this.guiTop + 187) {
                this.isSettingsTab = !this.isSettingsTab;
                ISpecialSlot c1 = (ISpecialSlot) this.getSlotAtPosition(180, 14);
                ISpecialSlot c2 = (ISpecialSlot) this.getSlotAtPosition(198, 14);
                ISpecialSlot c3 = (ISpecialSlot) this.getSlotAtPosition(216, 14);
                ISpecialSlot c4 = (ISpecialSlot) this.getSlotAtPosition(180, 32);
                ISpecialSlot c5 = (ISpecialSlot) this.getSlotAtPosition(198, 32);
                ISpecialSlot c6 = (ISpecialSlot) this.getSlotAtPosition(216, 32);
                ISpecialSlot c7 = (ISpecialSlot) this.getSlotAtPosition(180, 50);
                ISpecialSlot c8 = (ISpecialSlot) this.getSlotAtPosition(198, 50);
                ISpecialSlot c9 = (ISpecialSlot) this.getSlotAtPosition(216, 50);
                ISpecialSlot cO = (ISpecialSlot) this.getSlotAtPosition(217, 99);
                if(!this.isSettingsTab){
                    c1.setSlotExist(true);
                    c2.setSlotExist(true);
                    c3.setSlotExist(true);
                    c4.setSlotExist(true);
                    c5.setSlotExist(true);
                    c6.setSlotExist(true);
                    c7.setSlotExist(true);
                    c8.setSlotExist(true);
                    c9.setSlotExist(true);
                    cO.setSlotExist(true);
                    this.isSettingsTab = false;
                } else {
                    c1.setSlotExist(false);
                    c2.setSlotExist(false);
                    c3.setSlotExist(false);
                    c4.setSlotExist(false);
                    c5.setSlotExist(false);
                    c6.setSlotExist(false);
                    c7.setSlotExist(false);
                    c8.setSlotExist(false);
                    c9.setSlotExist(false);
                    cO.setSlotExist(false);
                    this.isSettingsTab = true;
                }
            }

            //Draw Module things:
            ItemStack module = NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
            if(module != null){
                if(module.getItem() instanceof IRarmorModule){
                    ((IRarmorModule) module.getItem()).onMouseClicked(mc, this, module, this.isSettingsTab, type, mouseX, mouseY);
                }
            }

            if(this.isSettingsTab){
                for(GuiCheckBox checkBox : checkBoxList){
                    if(checkBox.mouseClicked(mouseX, mouseY, this.guiLeft, this.guiTop)){
                        NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(player, 38, "SettingInWorldTooltip", checkBox.isClicked()));
                        NBTUtil.setBoolean(armor, "SettingInWorldTooltip", checkBox.isClicked());
                    }
                }
            }
        }

        try {
            super.mouseClicked(mouseX, mouseY, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isPointInRegion(int slotX, int slotY, int width, int height, int mouseX, int mouseY) {
        int k1 = this.guiLeft;
        int l1 = this.guiTop;
        mouseX -= k1;
        mouseY -= l1;
        boolean isAtCoordinates = mouseX >= slotX - 1 && mouseX < slotX + width + 1 && mouseY >= slotY - 1 && mouseY < slotY + height + 1;
        Slot slot = this.getSlotAtPosition(slotX, slotY);
        Slot moduleSlot = this.getSlotAtPosition(15, 34);
        if(this.isSettingsTab){
            if(slot instanceof SlotCrafting || slot instanceof SlotCraftingInput){
                return false;
            }
        }

        //Draw Module things:
        ItemStack module = NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if(module != null && moduleSlot != null){
            if(module.getItem() instanceof IRarmorModule){
                return ((IRarmorModule) module.getItem()).showSlot(mc, this, module, this.isSettingsTab, slot, mouseX, mouseY, slotX, slotY, isAtCoordinates);
            }
        } else {
            return isAtCoordinates && !(slot instanceof SlotModule);
        }

        return isAtCoordinates;
    }

    private Slot getSlotAtPosition(int x, int y) {
        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k) {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(k);
            if(x >= slot.xDisplayPosition - 1 && x < slot.xDisplayPosition + 16 + 1 && slot.yDisplayPosition >= y - 1 && slot.yDisplayPosition < y + 16 + 1){
                return slot;
            }
        }
        return null;
    }

    public int getGuiLeft(){
        return this.guiLeft;
    }
    public int getGuiTop(){
        return this.guiTop;
    }

}
