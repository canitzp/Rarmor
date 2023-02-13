/*
 * This file ("GuiRarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.inventory.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.inventory.ContainerRarmor;
import de.canitzp.rarmor.inventory.gui.button.TabButton;
import de.canitzp.rarmor.inventory.gui.button.TexturedButton;
import de.canitzp.rarmor.module.main.GuiModuleMain;
import de.canitzp.rarmor.event.ClientEvents;
import de.canitzp.rarmor.misc.Helper;
import mezz.jei.gui.TooltipRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.GuiUtils;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GuiRarmor extends EffectRenderingInventoryScreen<ContainerRarmor> {

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("gui_rarmor_base");

    private static double mouseX = 0;
    private static double mouseY = 0;
    public final TabButton[] tabButtons = new TabButton[10];
    private final IRarmorData currentData;
    private final RarmorModuleGui gui;
    private Button buttonBackToMainInventory;

    private boolean doesUpdateAnimate;
    private int updateTimer;

    public GuiRarmor(ContainerRarmor container, Inventory playerInventory, Component title){
        super(container, playerInventory, title);
        IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(Minecraft.getInstance().player, true);
        ActiveRarmorModule activeRarmorModule = data.getCurrentModules().get(data.getCurrentModules().size() <= data.getSelectedModule() ? 0 : data.getSelectedModule());
        this.currentData = activeRarmorModule.data;
        this.gui = activeRarmorModule.createGui();

        this.imageWidth = 236;
        this.imageHeight = 229;
    }
    
    @Override
    public void init(){
        super.init();

        // some hacky mouse positioning, because mc decides to center the mouse position, everytime a module is opened.
        GLFW.glfwSetCursorPos(Minecraft.getInstance().getWindow().getWindow(), mouseX, mouseY);

        for(int i = 0; i < this.tabButtons.length; i++){
            this.tabButtons[i] = new TabButton(this.leftPos+this.imageWidth-3, this.topPos+8+(i*21), new TranslatableComponent(""), button -> {
                if(button instanceof TabButton && this.currentData.getSelectedModule() != ((TabButton) button).moduleNum){
                    mouseX = Minecraft.getInstance().mouseHandler.xpos();
                    mouseY = Minecraft.getInstance().mouseHandler.ypos();
                    RarmorAPI.methodHandler.openRarmorFromClient(((TabButton) button).moduleNum, true, true);
                }
            });
            this.addRenderableWidget(this.tabButtons[i]);
        }
        this.updateTabs();
    
        this.buttonBackToMainInventory = new TexturedButton(this.leftPos+5, this.topPos+123, 20, 20, GuiModuleMain.RES_LOC, 0, 216, new TranslatableComponent(""), button -> {
            ClientEvents.stopGuiOverride = true;
            double mouseX = this.minecraft.mouseHandler.xpos();
            double mouseY = this.minecraft.mouseHandler.ypos();
            this.minecraft.player.closeContainer();
            this.minecraft.setScreen(new InventoryScreen(this.minecraft.player));
            GLFW.glfwSetCursorPos(this.minecraft.getWindow().getWindow(), mouseX, mouseY);
            ClientEvents.stopGuiOverride = false;
        });
        this.addRenderableWidget(this.buttonBackToMainInventory);
    
        this.gui.guiLeft = this.leftPos;
        this.gui.guiTop = this.topPos;
        this.gui.initGui();
    }
    
    @Override
    public void resize(Minecraft minecraft, int width, int height){
        super.resize(minecraft, width, height);
        this.gui.mc = minecraft;
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks){
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    
        if(this.buttonBackToMainInventory.isMouseOver(mouseX, mouseY)){
            this.renderTooltip(matrixStack, new TranslatableComponent(RarmorAPI.MOD_ID+".back"),mouseX, mouseY);
        }
        
        this.gui.drawScreen(mouseX, mouseY, partialTicks);
    
        if(this.gui.doesDisplayPowerBar()){
            if(mouseX >= this.leftPos+6+192 && mouseY >= this.topPos+5+3){
                if(mouseX < this.leftPos+6+192+31 && mouseY < this.topPos+5+3+132){
                    int current = this.currentData.getEnergyStored();
                    int max = this.currentData.getMaxEnergyStored();
                
                    List<Component> list = new ArrayList<>();
                    list.add(new TextComponent(ChatFormatting.GOLD.toString()).append(new TranslatableComponent(RarmorAPI.MOD_ID + ".storedEnergy")).append(" :"));
                    list.add(new TextComponent(ChatFormatting.YELLOW.toString() + current + "/" + max));
                    list.add(new TextComponent(ChatFormatting.ITALIC.toString() + (int)(((float)current / (float)max) * 100) + "%"));
                    this.renderTooltip(matrixStack, list, Optional.empty(), mouseX, mouseY);
                }
            }
        }
        
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y){
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, RES_LOC);
        
        blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    
        this.gui.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y);
    
        if(this.gui.doesDisplayPowerBar()){
            RenderSystem.setShaderTexture(0, GuiModuleMain.RES_LOC);
            blit(matrixStack, this.leftPos + 197, this.topPos + 7, 191, 2, 33, 134);
        
            int i = this.currentData.getEnergyStored()*132/this.currentData.getMaxEnergyStored();
            if(i > 0){
                float[] colors = Helper.getColor(this.minecraft.level.getGameTime() % 256);
                RenderSystem.setShaderColor(colors[0]/255F, colors[1]/255F, colors[2]/255F, 1.0F);
                blit(matrixStack, this.leftPos + 198, this.topPos + 140 - i, 224, 134 - i + 1, 31, i);
            }
        }
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        if(super.mouseClicked(mouseX, mouseY, button)){
            return true;
        } else {
            return this.gui.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button){
        if(super.mouseReleased(mouseX, mouseY, button)){
            return true;
        } else {
            return this.gui.mouseReleased(mouseX, mouseY, button);
        }
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY){
        if(super.mouseDragged(mouseX, mouseY, button, dragX, dragY)){
            return true;
        } else {
            return this.gui.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    public void updateTabs(){
        int buttonCounter = 0;

        List<ActiveRarmorModule> modules = this.currentData.getCurrentModules();
        for(int i = 0; i < modules.size(); i++){
            if(i < this.tabButtons.length){
                ActiveRarmorModule module = modules.get(i);
                if(module != null && module.hasTab(Minecraft.getInstance().player)){
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
    public void containerTick(){
        super.containerTick();
        this.gui.updateScreen();
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().player.closeContainer();
    }
}
