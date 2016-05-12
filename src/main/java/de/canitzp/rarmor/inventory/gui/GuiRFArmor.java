/*
 * This file 'GuiRFArmor.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.inventory.gui;

import com.google.common.collect.Lists;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.Colors;
import de.canitzp.rarmor.api.RarmorResources;
import de.canitzp.rarmor.api.gui.GuiButton;
import de.canitzp.rarmor.api.gui.GuiCheckBox;
import de.canitzp.rarmor.api.gui.GuiColorBox;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.integration.CraftingTweaksIntegration;
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
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public class GuiRFArmor extends GuiContainerBase{

    public ResourceLocation modulesGui = RarmorResources.RARMORMODULEGUI.getNewLocation();
    public ResourceLocation checkBox = RarmorResources.GUIELEMENTS.getNewLocation();
    public boolean isSettingsTab, isColoringTab;
    private EntityPlayer player;
    private ItemStack armor;
    private ResourceLocation normalGui = RarmorResources.RARMORGUI.getNewLocation();
    private float xSizeFloat;
    private float ySizeFloat;
    private List<GuiCheckBox> checkBoxList = Lists.newArrayList();
    private List<GuiColorBox> colorBoxes = new ArrayList<>();
    public EntityEquipmentSlot currentColorSlot;
    private Map<EntityEquipmentSlot, GuiButton> buttons = new HashMap<>();

    public GuiRFArmor(EntityPlayer player, ContainerRFArmor containerRFArmor){
        super(containerRFArmor);
        this.xSize = 247;
        this.ySize = 226;
        this.armor = RarmorUtil.getArmor(player, EntityEquipmentSlot.CHEST);
        this.player = player;
        this.allowUserInput = true;
    }

    @Override
    public void initGui(){
        //Settings Tab:
        GuiCheckBox setInWorldTooltip = new GuiCheckBox(this, this.checkBox, 117, 12, 10, "Show In-World Tooltips", JavaUtil.newList("Show Tooltips like", "the Amount of Fluid in Tanks"));
        GuiCheckBox haveToSneak = new GuiCheckBox(this, this.checkBox, 117, 24, 10, "Sneak to open Gui", JavaUtil.newList("If this is activated", "you have to sneak (press shift)", "to open the Rarmor Gui."));
        setInWorldTooltip.setClicked(NBTUtil.getBoolean(armor, "SettingInWorldTooltip"));
        haveToSneak.setClicked(NBTUtil.getBoolean(armor, "HaveToSneakToOpenGui"));
        checkBoxList.add(setInWorldTooltip);
        checkBoxList.add(haveToSneak);

        //Coloring Tab:
        int x = 117, y = 9;
        for (int i = 0; i < Colors.values().length; i++){
            this.colorBoxes.add(new GuiColorBox(Colors.values()[i], x, y));
            x += 9;
            if(x > 117 + 6*8){
                x = 117;
                y += 9;
            }
        }
        this.buttons.put(EntityEquipmentSlot.HEAD, new GuiButton(44, 10, 16, 16));
        this.buttons.put(EntityEquipmentSlot.CHEST, new GuiButton(44, 28, 16, 16));
        this.buttons.put(EntityEquipmentSlot.LEGS, new GuiButton(44, 46, 16, 16));
        this.buttons.put(EntityEquipmentSlot.FEET, new GuiButton(44, 64, 16, 16));

        //Module Stuff:
        ItemStack module = NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
        if(module != null){
            if(module.getItem() instanceof IRarmorModule){
                ((IRarmorModule) module.getItem()).initGui(this.mc, armor, this);
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
        ItemStack module = RarmorUtil.getRarmorModule(player);
        if(module != null){
            if(module.getItem() instanceof IRarmorModule){
                ((IRarmorModule) module.getItem()).drawGuiContainerBackgroundLayer(mc, this, this.armor, module, this.isSettingsTab, this.isColoringTab, par1, par2, par3);
            }
        }

        if(this.isSettingsTab){
            this.mc.getTextureManager().bindTexture(modulesGui);
            this.drawTexturedModalRect(this.guiLeft + 115, this.guiTop + 3, 114, 0, 129, 130);
            for(GuiCheckBox checkBox : checkBoxList){
                checkBox.drawCheckBox(this.guiLeft, this.guiTop);
            }
        } else {
            this.craftingSlot(false);
            if(CraftingTweaksIntegration.isActive){
                this.mc.getTextureManager().bindTexture(modulesGui);
                this.drawTexturedModalRect(this.guiLeft + 244, this.guiTop + 7, 26, 129, 12, 67);
            }
        }

        if(this.isColoringTab) {
            for (GuiColorBox colorBox : this.colorBoxes) {
                colorBox.render(this);
            }
            if (this.currentColorSlot != null) {
                this.buttons.get(this.currentColorSlot).render(this);
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
        if(stack != null){
            if(stack.getItem() instanceof IRarmorModule){
                ((IRarmorModule) stack.getItem()).drawScreen(mc, this, this.armor, stack, this.isSettingsTab, this.isColoringTab,  renderPartialTicks, mouseX, mouseY);
            }
        }

        //Settings Tab
        if(this.isSettingsTab){
            for(GuiCheckBox checkBox : checkBoxList){
                checkBox.mouseOverEvent(mouseX, mouseY, this.guiLeft, this.guiTop);
            }
        }

        //Coloring Tab
        if(this.isColoringTab){
            for(GuiColorBox colorBox : colorBoxes){
                colorBox.drawScreen(this, mouseX, mouseY);
            }
        }

        if(mouseX >= this.guiLeft + 18 && mouseY >= this.guiTop + 8 && mouseX <= this.guiLeft + 27 && mouseY <= this.guiTop + 28){
            int energy = ((ItemRFArmorBody) armor.getItem()).getEnergyStored(armor);
            int cap = ((ItemRFArmorBody) armor.getItem()).getMaxEnergyStored(armor);
            this.drawHoveringText(Lists.newArrayList(Integer.toString(energy * 4) + "/" + Integer.toString(cap * 4) + " RF"), mouseX, mouseY);
        } else if(mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 140 && mouseX <= this.guiLeft + 35 && mouseY <= this.guiTop + 161){
            this.drawHoveringText(Lists.newArrayList("Back to normal Inventory"), mouseX, mouseY);
        } else if(mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 166 && mouseX <= this.guiLeft + 35 && mouseY <= this.guiTop + 187){
            this.drawHoveringText(Lists.newArrayList("Settings"), mouseX, mouseY);
        } else if(mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 192 && mouseX <= this.guiLeft + 35 && mouseY <= this.guiTop + 213){
            this.drawHoveringText(JavaUtil.newList("Rarmor painter.", "Click on your Armor parts to start."), mouseX, mouseY);
        } else if(mouseX >= this.guiLeft + 33 && mouseY >= this.guiTop + 33 && mouseX <= this.guiLeft + 36 && mouseY <= this.guiTop + 40){
            ItemStack module = RarmorUtil.readRarmor(player).getStackInSlot(ItemRFArmorBody.MODULESLOT);
            if(module != null && module.getItem() instanceof IRarmorModule){
                if(((IRarmorModule) module.getItem()).getGuiHelp() != null){
                    this.drawHoveringText(((IRarmorModule) module.getItem()).getGuiHelp(), mouseX, mouseY);
                } else if(((IRarmorModule) module.getItem()).getDescription(player, module, false) != null){
                    this.drawHoveringText(fontRendererObj.listFormattedStringToWidth(((IRarmorModule) module.getItem()).getDescription(player, module, false).replace(TextFormatting.GRAY.toString(), TextFormatting.RESET.toString()), 350), mouseX, mouseY);
                } else {
                    this.drawHoveringText(JavaUtil.newList("This Module doesn't provide", "a help page."), mouseX, mouseY);
                }
            }
        }

        if(ClientProxy.specialPlayers.containsKey(player.getName())){
            Pair<String, Colors> pair = ClientProxy.specialPlayers.get(player.getName());
            this.drawCenteredString(fontRendererObj, pair.getKey(), this.guiLeft + (this.xSize / 2), this.guiTop - 18, pair.getValue().colorValue);
            this.drawCenteredString(fontRendererObj, "Thank you " + player.getName(), this.guiLeft + (this.xSize / 2), this.guiTop - 8, pair.getValue().colorValue);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int type){
        if(type == 0){
            if(mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 140 && mouseX <= this.guiLeft + 35 && mouseY <= this.guiTop + 161){
                NBTUtil.setBoolean(this.armor, "click", true);
                this.onGuiClosed();
                this.mc.thePlayer.sendQueue.addToSendQueue(new CPacketCloseWindow(this.mc.thePlayer.openContainer.windowId));
                GuiInventory inventory = new GuiInventory(this.mc.thePlayer);
                this.mc.displayGuiScreen(inventory);
            }
            if(mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 166 && mouseX <= this.guiLeft + 35 && mouseY <= this.guiTop + 187){
                this.isSettingsTab = !this.isSettingsTab;
                this.isColoringTab = false;
            }
            if(mouseX >= this.guiLeft + 15 && mouseY >= this.guiTop + 192 && mouseX <= this.guiLeft + 35 && mouseY <= this.guiTop + 213){
                this.isColoringTab = !this.isColoringTab;
                this.isSettingsTab = false;
            }

            if(mouseX >= this.guiLeft + 44 && mouseY >= this.guiTop + 10 && mouseX <= this.guiLeft + 59 && mouseY <= this.guiTop + 25){
                this.currentColorSlot = EntityEquipmentSlot.HEAD;
            } else if(mouseX >= this.guiLeft + 44 && mouseY >= this.guiTop + 28 && mouseX <= this.guiLeft + 59 && mouseY <= this.guiTop + 43){
                this.currentColorSlot = EntityEquipmentSlot.CHEST;
            } else if(mouseX >= this.guiLeft + 44 && mouseY >= this.guiTop + 46 && mouseX <= this.guiLeft + 59 && mouseY <= this.guiTop + 61){
                this.currentColorSlot = EntityEquipmentSlot.LEGS;
            } else if(mouseX >= this.guiLeft + 44 && mouseY >= this.guiTop + 64 && mouseX <= this.guiLeft + 59 && mouseY <= this.guiTop + 79){
                this.currentColorSlot = EntityEquipmentSlot.FEET;
            } else if(!(mouseX >= this.guiLeft + 115 && mouseY >= this.guiTop + 9 && mouseX <= this.guiLeft + 175 && mouseY <= this.guiTop + 60)){
                this.currentColorSlot = null;
            }

            if(this.isColoringTab) {
                for (GuiColorBox colorBox : this.colorBoxes) {
                    Colors c = colorBox.onClick(this, mouseX, mouseY);
                    if(c != null && this.currentColorSlot != null){
                        RarmorUtil.paintRarmor(player, this.currentColorSlot, c);
                    }
                }
            }

            craftingSlot(this.isSettingsTab);

            //Draw Module things:
            ItemStack module = RarmorUtil.readRarmor(armor).getStackInSlot(ItemRFArmorBody.MODULESLOT);
            if(module != null){
                if(module.getItem() instanceof IRarmorModule){
                    ((IRarmorModule) module.getItem()).onMouseClicked(mc, this, this.armor, module, this.isSettingsTab, this.isColoringTab, type, mouseX, mouseY);
                }
            }

            if(this.isSettingsTab){
                for(GuiCheckBox checkBox : checkBoxList){
                    if(checkBox.mouseClicked(mouseX, mouseY, this.guiLeft, this.guiTop)){
                        if(checkBox.text.equals("Show In-World Tooltips")){
                            NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(player, 38, "SettingInWorldTooltip", checkBox.isClicked()));
                            NBTUtil.setBoolean(armor, "SettingInWorldTooltip", checkBox.isClicked());
                        } else if (checkBox.text.equals("Sneak to open Gui")){
                            NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(player, 38, "HaveToSneakToOpenGui", checkBox.isClicked()));
                            NBTUtil.setBoolean(armor, "HaveToSneakToOpenGui", checkBox.isClicked());
                        }
                    }
                }
            }
        }

        try{
            super.mouseClicked(mouseX, mouseY, type);
        } catch(IOException e){
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
        if(isAtCoordinates){
            for(Slot slotUpdate : deactivatedSlots){
                if(slotUpdate != null){
                    if(slotUpdate.xDisplayPosition == slotX && slotUpdate.yDisplayPosition == slotY){
                        return false;
                    }
                }
            }

        }
        return isAtCoordinates;
    }

    public int getGuiLeft(){
        return super.guiLeft;
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
        RarmorUtil.toggleSlotInGui(116, 64, activated);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void toggleBtn(int x, int y, boolean value){
        Pair<Integer, Integer> pair = Pair.of(x, y);
        if(value){
            if(!deactivatedButtons.contains(pair)){
                deactivatedButtons.add(pair);
            }
        } else {
            if(deactivatedButtons.contains(pair)){
                deactivatedButtons.remove(pair);
            }
        }
    }

}
