/*
 * This file ("GuiRarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.inventory.gui;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.Rarmor;
import de.ellpeck.rarmor.mod.event.ClientEvents;
import de.ellpeck.rarmor.mod.inventory.ContainerRarmor;
import de.ellpeck.rarmor.mod.inventory.gui.button.TabButton;
import de.ellpeck.rarmor.mod.inventory.gui.button.TexturedButton;
import de.ellpeck.rarmor.mod.misc.Helper;
import de.ellpeck.rarmor.mod.module.main.GuiModuleMain;
import de.ellpeck.rarmor.mod.packet.PacketHandler;
import de.ellpeck.rarmor.mod.packet.PacketOpenModule;
import de.ellpeck.rarmor.mod.update.UpdateChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiRarmor extends GuiContainer{

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("guiRarmorBase");

    private final IRarmorData currentData;
    private final RarmorModuleGui gui;
    private final TabButton[] tabButtons = new TabButton[10];
    private GuiButton buttonBackToMainInventory;

    private TexturedButton updateButton;
    private boolean doesUpdateAnimate;
    private int updateTimer;
    private List<String> updateDisplayStrings;

    public GuiRarmor(ContainerRarmor container, ActiveRarmorModule currentModule){
        super(container);
        this.currentData = currentModule.data;
        this.gui = currentModule.createGui(this);

        this.xSize = 236;
        this.ySize = 229;
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height){
        super.setWorldAndResolution(mc, width, height);
        this.gui.mc = this.mc;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        this.gui.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.gui.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state){
        super.mouseReleased(mouseX, mouseY, state);
        this.gui.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.gui.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException{
        super.actionPerformed(button);

        for(TabButton tabButton : this.tabButtons){
            if(tabButton == button && this.currentData.getSelectedModule() != tabButton.moduleNum){
                this.currentData.selectModule(tabButton.moduleNum);
                PacketHandler.handler.sendToServer(new PacketOpenModule(this.currentData.getSelectedModule(), true, true));
            }
        }

        if(button == this.buttonBackToMainInventory){
            ClientEvents.stopGuiOverride = true;
            int mouseX = Mouse.getX();
            int mouseY = Mouse.getY();
            this.mc.thePlayer.closeScreen();
            this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
            Mouse.setCursorPosition(mouseX, mouseY);
            ClientEvents.stopGuiOverride = false;
        }

        if(!UpdateChecker.notifiedAlready && button == this.updateButton){
            if(UpdateChecker.checkFailed || isCtrlKeyDown()){
                this.updateButton.visible = false;
                UpdateChecker.notifiedAlready = true;
            }
            else{
                String url;
                if(isShiftKeyDown()){
                    url = UpdateChecker.DOWNLOAD_LINK;
                }
                else{
                    url = UpdateChecker.CHANGELOG_LINK;
                }

                if(Desktop.isDesktopSupported()){
                    try{
                        Desktop.getDesktop().browse(new URI(url));
                    }
                    catch(Exception e){
                        Rarmor.LOGGER.error("Couldn't open URL!", e);
                    }
                }
            }
        }

        this.gui.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);

        if(this.buttonBackToMainInventory.isMouseOver()){
            GuiUtils.drawHoveringText(Collections.singletonList(I18n.format(RarmorAPI.MOD_ID+".back")), mouseX, mouseY, this.mc.displayWidth, this.mc.displayHeight, -1, this.mc.fontRendererObj);
        }

        if(this.updateButton.visible && this.updateButton.isMouseOver() && this.updateDisplayStrings != null){
            GuiUtils.drawHoveringText(this.updateDisplayStrings, mouseX, mouseY, this.mc.displayWidth, this.mc.displayHeight, -1, this.mc.fontRendererObj);
        }

        this.gui.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui(){
        super.initGui();

        for(int i = 0; i < this.tabButtons.length; i++){
            this.tabButtons[i] = new TabButton(2837+i, this.guiLeft+this.xSize-3, this.guiTop+8+(i*21));
            this.buttonList.add(this.tabButtons[i]);
        }
        this.updateTabs();

        this.buttonBackToMainInventory = new TexturedButton(2836, this.guiLeft+5, this.guiTop+120, 20, 20, GuiModuleMain.RES_LOC, 0, 216);
        this.buttonList.add(this.buttonBackToMainInventory);

        this.updateButton = new TexturedButton(1337, this.guiLeft-21, this.guiTop-21, 20, 20, GuiModuleMain.RES_LOC, 216, 216);
        this.buttonList.add(this.updateButton);
        this.initUpdateButton();

        this.gui.guiLeft = this.guiLeft;
        this.gui.guiTop = this.guiTop;
        this.gui.buttonList = this.buttonList;
        this.gui.initGui();
    }

    private void initUpdateButton(){
        boolean failed = UpdateChecker.checkFailed;
        boolean notify = UpdateChecker.needsUpdateNotify;
        if(!UpdateChecker.notifiedAlready && (failed || notify)){
            this.updateDisplayStrings = new ArrayList<String>();

            if(failed){
                this.updateDisplayStrings.add(TextFormatting.RED+I18n.format(RarmorAPI.MOD_ID+".checkFailed", TextFormatting.DARK_GREEN+Rarmor.MOD_NAME+TextFormatting.RED));
            }
            else if(notify){
                this.updateDisplayStrings.add(TextFormatting.GOLD+I18n.format(RarmorAPI.MOD_ID+".notifyUpdate.1", TextFormatting.DARK_GREEN+Rarmor.MOD_NAME+TextFormatting.GOLD));
                this.updateDisplayStrings.add(I18n.format(RarmorAPI.MOD_ID+".notifyUpdate.2", TextFormatting.RED+Rarmor.VERSION+TextFormatting.RED));
                this.updateDisplayStrings.add(I18n.format(RarmorAPI.MOD_ID+".notifyUpdate.3", TextFormatting.GREEN+UpdateChecker.updateVersionString+TextFormatting.RED));
            }

            this.updateDisplayStrings.add("");
            for(int i = (notify ? 0 : 2); i < 3; i++){
                this.updateDisplayStrings.add(TextFormatting.ITALIC+I18n.format(RarmorAPI.MOD_ID+".clickInfo."+(i+1)));
            }

            this.updateButton.visible = true;
        }
        else{
            this.updateButton.visible = false;
        }
    }

    public void updateTabs(){
        int buttonCounter = 0;

        List<ActiveRarmorModule> modules = this.currentData.getCurrentModules();
        for(int i = 0; i < modules.size(); i++){
            if(i < this.tabButtons.length){
                ActiveRarmorModule module = modules.get(i);
                if(module != null && module.hasTab(this.mc.thePlayer)){
                    this.tabButtons[buttonCounter].setModule(this.currentData, i);
                    this.tabButtons[buttonCounter].visible = true;
                    buttonCounter++;
                }
            }
            else{
                break;
            }
        }

        while(buttonCounter < this.tabButtons.length){
            this.tabButtons[buttonCounter].visible = false;
            buttonCounter++;
        }
    }

    @Override
    public void updateScreen(){
        super.updateScreen();

        if(this.updateButton.visible){
            this.updateTimer++;
            if(this.doesUpdateAnimate){
                if(this.updateTimer%40 == 0){
                    this.updateButton.u = 216;
                }
                else if(this.updateTimer%20 == 0){
                    this.updateButton.u = 236;
                }
            }
        }

        this.gui.updateScreen();
    }
}
