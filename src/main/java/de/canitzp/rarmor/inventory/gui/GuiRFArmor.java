package de.canitzp.rarmor.inventory.gui;

import com.google.common.collect.Lists;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.RarmorResources;
import de.canitzp.rarmor.api.gui.GuiCheckBox;
import de.canitzp.rarmor.api.gui.GuiContainerBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.integration.craftingTweaks.CraftingTweaksIntegration;
import de.canitzp.rarmor.inventory.container.ContainerRFArmor;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.network.ClientProxy;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketSendNBTBoolean;
import de.canitzp.rarmor.util.*;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

/**
 * @author canitzp
 */
public class GuiRFArmor extends GuiContainerBase{

    public ResourceLocation modulesGui = RarmorResources.RARMORMODULEGUI.getNewLocation();
    public ResourceLocation checkBox = RarmorResources.GUIELEMENTS.getNewLocation();
    public boolean isSettingsTab;
    private EntityPlayer player;
    private ItemStack armor;
    private ResourceLocation normalGui = RarmorResources.RARMORGUI.getNewLocation();
    private float xSizeFloat;
    private float ySizeFloat;
    private List<GuiCheckBox> checkBoxList = Lists.newArrayList();

    public GuiRFArmor(EntityPlayer player, ContainerRFArmor containerRFArmor){
        super(containerRFArmor);
        this.xSize = 247;
        this.ySize = 226;
        this.armor = PlayerUtil.getArmor(player, EntityEquipmentSlot.CHEST);
        this.player = player;
    }

    @Override
    public void initGui(){
        GuiCheckBox setInWorldTooltip = new GuiCheckBox(this, this.checkBox, 117, 12, 10, "Show In-World Tooltips", JavaUtil.newList("Show Tooltips like", "the Amount of Fluid in Tanks"));
        setInWorldTooltip.setClicked(NBTUtil.getBoolean(armor, "SettingInWorldTooltip"));
        checkBoxList.add(setInWorldTooltip);
        ItemStack module = NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if (module != null){
            if (module.getItem() instanceof IRarmorModule){
                ((IRarmorModule) module.getItem()).initGui(player.getEntityWorld(), player, armor, this, checkBoxList, checkBox);
            }
        }

        this.deactivatedSlots.add(SlotUtil.getSlotAtPosition(this, 140, 18));
        this.deactivatedSlots.add(SlotUtil.getSlotAtPosition(this, -16, 14));
        this.deactivatedSlots.add(SlotUtil.getSlotAtPosition(this, -16, 34));
        this.deactivatedSlots.add(SlotUtil.getSlotAtPosition(this, -16, 54));
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(normalGui);
        //Draw Gui:
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        int energy = EnergyUtil.getEnergy(this.armor);
        int factorOfBurnTime = (int) (NBTUtil.getInteger(armor, "BurnTime") * 11.9 / 200);
        //Draw Batterie:
        GuiUtil.drawBarHorizontal(normalGui, normalGui, this.guiLeft + 18, this.guiTop + 29, 55, 247, 10, 21, this.armor.getMaxDamage(), energy);
        //Draw Furnace Burn Time:
        this.drawTexturedModalRect(this.guiLeft + 15, this.guiTop + 89 - factorOfBurnTime, 39, 237 - factorOfBurnTime, 16, factorOfBurnTime);

        this.mc.getTextureManager().bindTexture(checkBox);
        this.drawTexturedModalRect(this.guiLeft + 33, this.guiTop + 33, 15, 0, 3, 7);

        //Draw Module things:

        ItemStack module = RarmorUtil.readRarmor(armor).getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if (module != null){
            if (module.getItem() instanceof IRarmorModule){
                ((IRarmorModule) module.getItem()).drawGuiContainerBackgroundLayer(mc, this, this.armor, module, this.isSettingsTab, par1, par2, par3, guiLeft, guiTop);
            }
        }



        if (this.isSettingsTab){
            this.mc.getTextureManager().bindTexture(modulesGui);
            this.drawTexturedModalRect(this.guiLeft + 115, this.guiTop + 3, 114, 0, 129, 130);
            for (GuiCheckBox checkBox : checkBoxList){
                checkBox.drawCheckBox(this.guiLeft, this.guiTop);
            }
        }else{
            this.craftingSlot(false);
            if (CraftingTweaksIntegration.isActive){
                this.mc.getTextureManager().bindTexture(modulesGui);
                this.drawTexturedModalRect(this.guiLeft + 244, this.guiTop + 7, 26, 129, 12, 67);
            }
        }

        GuiInventory.drawEntityOnScreen(this.guiLeft + 88, this.guiTop + 74, 30, (float) (this.guiLeft + 88) - this.xSizeFloat, (float) (this.guiTop + 72 - 50) - this.ySizeFloat, this.mc.thePlayer);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks){
        this.xSizeFloat = (float) mouseX;
        this.ySizeFloat = (float) mouseY;
        super.drawScreen(mouseX, mouseY, renderPartialTicks);
        ItemStack stack = RarmorUtil.readRarmor(armor).getStackInSlot(ItemRFArmorBody.MODULESLOT);

        //Draw Module things:
        if (stack != null){
            if (stack.getItem() instanceof IRarmorModule){
                ((IRarmorModule) stack.getItem()).drawScreen(mc, this, this.armor, stack, this.isSettingsTab, renderPartialTicks, mouseX, mouseY);
            }
        }

        if (this.isSettingsTab){
            for (GuiCheckBox checkBox : checkBoxList){
                checkBox.mouseOverEvent(mouseX, mouseY, this.guiLeft, this.guiTop, fontRendererObj);
            }
        }

        if (mouseX >= this.guiLeft + 18 && mouseY >= this.guiTop + 8 && mouseX <= this.guiLeft + 27 && mouseY <= this.guiTop + 28){
            int energy = EnergyUtil.getEnergy(this.armor);
            int cap = ((ItemRFArmorBody) armor.getItem()).getMaxEnergyStored(armor);
            this.drawHoveringText(Lists.newArrayList(Integer.toString(energy) + "/" + Integer.toString(cap) + " RF"), mouseX, mouseY, this.fontRendererObj);
        }
        if (mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 140 && mouseX <= this.guiLeft + 35 && mouseY <= this.guiTop + 161){
            this.drawHoveringText(Lists.newArrayList("Back to normal Inventory"), mouseX, mouseY, this.fontRendererObj);
        }
        if (mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 166 && mouseX <= this.guiLeft + 35 && mouseY <= this.guiTop + 187){
            this.drawHoveringText(Lists.newArrayList("Settings"), mouseX, mouseY, this.fontRendererObj);
        }
        if (mouseX >= this.guiLeft + 33 && mouseY >= this.guiTop + 33 && mouseX <= this.guiLeft + 36 && mouseY <= this.guiTop + 40){
            IRarmorModule module = RarmorUtil.getRarmorModule(player);
            if (module != null){
                if (module.getGuiHelp() != null){
                    this.drawHoveringText(module.getGuiHelp(), mouseX, mouseY);
                }else{
                    this.drawHoveringText(JavaUtil.newList("This Module doesn't provide", "a help page."), mouseX, mouseY);
                }
            }
        }

        if (ClientProxy.specialPlayers.containsKey(player.getName())){
            Pair<String, Integer> pair = ClientProxy.specialPlayers.get(player.getName());
            this.drawCenteredString(fontRendererObj, pair.getKey(), this.guiLeft + (this.xSize / 2), this.guiTop - 18, pair.getValue());
            this.drawCenteredString(fontRendererObj, player.getName(), this.guiLeft + (this.xSize / 2), this.guiTop - 8, pair.getValue());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int type){
        if (type == 0){
            if (mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 140 && mouseX <= this.guiLeft + 35 && mouseY <= this.guiTop + 161){
                this.armor.getTagCompound().setBoolean("click", true);
                this.onGuiClosed();
                this.mc.thePlayer.sendQueue.addToSendQueue(new CPacketCloseWindow(this.mc.thePlayer.openContainer.windowId));
                GuiInventory inventory = new GuiInventory(this.mc.thePlayer);
                this.mc.displayGuiScreen(inventory);
            }
            if (mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 166 && mouseX <= this.guiLeft + 35 && mouseY <= this.guiTop + 187){
                this.isSettingsTab = !this.isSettingsTab;
            }

            craftingSlot(this.isSettingsTab);
            //Draw Module things:
            ItemStack module = RarmorUtil.readRarmor(armor).getStackInSlot(ItemRFArmorBody.MODULESLOT);
            if (module != null){
                if(module.getItem() instanceof IRarmorModule){
                    ((IRarmorModule) module.getItem()).onMouseClicked(mc, this, this.armor, module, this.isSettingsTab, type, mouseX, mouseY, this.guiLeft, this.guiTop);
                }
            }

            if (this.isSettingsTab){
                for (GuiCheckBox checkBox : checkBoxList){
                    if (checkBox.mouseClicked(mouseX, mouseY, this.guiLeft, this.guiTop)){
                        NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(player, 38, "SettingInWorldTooltip", checkBox.isClicked()));
                        NBTUtil.setBoolean(armor, "SettingInWorldTooltip", checkBox.isClicked());
                    }
                }
            }
        }

        try{
            super.mouseClicked(mouseX, mouseY, type);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isPointInRegion(int slotX, int slotY, int width, int height, int mouseX, int mouseY){
        int k1 = this.guiLeft;
        int l1 = this.guiTop;
        mouseX -= k1;
        mouseY -= l1;
        boolean isAtCoordinates = mouseX >= slotX - 1 && mouseX < slotX + width + 1 && mouseY >= slotY - 1 && mouseY < slotY + height + 1;
        if (isAtCoordinates){
            for (Slot slotUpdate : deactivatedSlots){
                if (slotUpdate != null){
                    if (slotUpdate.xDisplayPosition == slotX && slotUpdate.yDisplayPosition == slotY){
                        return false;
                    }
                }
            }

        }
        return isAtCoordinates;
    }

    public int getGuiLeft(){
        return this.guiLeft;
    }

    public int getGuiTop(){
        return this.guiTop;
    }

    public void craftingSlot(boolean activated){
        RarmorUtil.toggleSlotInGui(180, 14, activated);
        RarmorUtil.toggleSlotInGui(198, 14, activated);
        RarmorUtil.toggleSlotInGui(216, 14, activated);
        RarmorUtil.toggleSlotInGui(180, 32, activated);
        RarmorUtil.toggleSlotInGui(198, 32, activated);
        RarmorUtil.toggleSlotInGui(216, 32, activated);
        RarmorUtil.toggleSlotInGui(180, 50, activated);
        RarmorUtil.toggleSlotInGui(198, 50, activated);
        RarmorUtil.toggleSlotInGui(216, 50, activated);
        RarmorUtil.toggleSlotInGui(217, 99, activated);
        toggleBtn(235, 14, activated);
        toggleBtn(235, 32, activated);
        toggleBtn(235, 50, activated);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void toggleBtn(int x, int y, boolean value){
        Pair<Integer, Integer> pair = Pair.of(x, y);
        if (value){
            if (!deactivatedButtons.contains(pair)){
                deactivatedButtons.add(pair);
            }
        }else{
            if (deactivatedButtons.contains(pair)){
                deactivatedButtons.remove(pair);
            }
        }
    }

}
