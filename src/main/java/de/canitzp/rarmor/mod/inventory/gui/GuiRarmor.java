/*
 * This file ("GuiRarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.inventory.gui;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.mod.inventory.ContainerRarmor;
import de.canitzp.rarmor.mod.inventory.gui.button.TabButton;
import de.canitzp.rarmor.mod.misc.Helper;
import de.canitzp.rarmor.mod.packet.PacketHandler;
import de.canitzp.rarmor.mod.packet.PacketOpenModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiRarmor extends GuiContainer{

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("guiRarmorBase");

    private final IRarmorData currentData;
    private final RarmorModuleGui gui;
    private final TabButton[] tabButtons = new TabButton[10];

    public GuiRarmor(ContainerRarmor container, ActiveRarmorModule currentModule, IRarmorData currentData){
        super(container);
        this.currentData = currentData;
        this.gui = currentModule.createGui(this, currentData);

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
                PacketHandler.handler.sendToServer(new PacketOpenModule(this.currentData.getSelectedModule(), true));
                System.out.println("OPENING!!");
            }
        }

        this.gui.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.gui.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui(){
        super.initGui();

        for(int i = 0; i < this.tabButtons.length; i++){
            this.tabButtons[i] = new TabButton(-2837+i, this.guiLeft+this.xSize-3, this.guiTop+8+(i*21));
            this.buttonList.add(this.tabButtons[i]);
        }
        this.updateTabs();

        this.gui.guiLeft = this.guiLeft;
        this.gui.guiTop = this.guiTop;
        this.gui.buttonList = this.buttonList;
        this.gui.initGui();
    }

    public void updateTabs(){
        int buttonCounter = 0;

        List<ActiveRarmorModule> modules = this.currentData.getCurrentModules();
        System.out.println(modules);
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
        this.gui.updateScreen();
    }
}
