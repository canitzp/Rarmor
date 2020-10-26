/*
 * This file ("GuiRarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.inventory.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiRarmor extends DisplayEffectsScreen<ContainerRarmor> {

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("gui_rarmor_base");
    public final TabButton[] tabButtons = new TabButton[10];
    private final IRarmorData currentData;
    private final RarmorModuleGui gui;
    private Button buttonBackToMainInventory;

    private boolean doesUpdateAnimate;
    private int updateTimer;

    public GuiRarmor(ContainerRarmor container, PlayerInventory playerInventory, ITextComponent title){
        super(container, playerInventory, title);
        IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(Minecraft.getInstance().player, true);
        ActiveRarmorModule activeRarmorModule = data.getCurrentModules().get(data.getCurrentModules().size() <= data.getSelectedModule() ? 0 : data.getSelectedModule());
        this.currentData = activeRarmorModule.data;
        this.gui = activeRarmorModule.createGui();

        this.xSize = 236;
        this.ySize = 229;
    }
    
    @Override
    public void init(Minecraft minecraft, int width, int height){
        super.init(minecraft, width, height);
    
        for(int i = 0; i < this.tabButtons.length; i++){
            this.tabButtons[i] = new TabButton(this.guiLeft+this.xSize-3, this.guiTop+8+(i*21), new TranslationTextComponent(""), button -> {
                if(button instanceof TabButton && this.currentData.getSelectedModule() != ((TabButton) button).moduleNum){
                    RarmorAPI.methodHandler.openRarmorFromClient(((TabButton) button).moduleNum, true, true);
                }
            });
            this.buttons.add(this.tabButtons[i]);
        }
        this.updateTabs();
    
        this.buttonBackToMainInventory = new TexturedButton(this.guiLeft+5, this.guiTop+123, 20, 20, GuiModuleMain.RES_LOC, 0, 216, new TranslationTextComponent(""), button -> {
            ClientEvents.stopGuiOverride = true;
            double mouseX = this.minecraft.mouseHelper.getMouseX();
            double mouseY = this.minecraft.mouseHelper.getMouseY();
            this.minecraft.player.closeScreen();
            this.minecraft.displayGuiScreen(new InventoryScreen(this.minecraft.player));
            GLFW.glfwSetCursorPos(this.minecraft.getMainWindow().getHandle(), mouseX, mouseY);
            ClientEvents.stopGuiOverride = false;
        });
        this.buttons.add(this.buttonBackToMainInventory);
    
        this.gui.guiLeft = this.guiLeft;
        this.gui.guiTop = this.guiTop;
        this.gui.buttonList = this.buttons;
        this.gui.initGui();
    }
    
    @Override
    public void resize(Minecraft minecraft, int width, int height){
        super.resize(minecraft, width, height);
        this.gui.mc = this.minecraft;
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    
        if(this.buttonBackToMainInventory.isMouseOver(mouseX, mouseY)){
            GuiUtils.drawHoveringText(matrixStack, Collections.singletonList(new TranslationTextComponent(RarmorAPI.MOD_ID+".back")),mouseX, mouseY, this.minecraft.getMainWindow().getWidth(), this.minecraft.getMainWindow().getHeight(), -1, this.minecraft.fontRenderer);
        }
        
        this.gui.drawScreen(mouseX, mouseY, partialTicks);
    
        if(this.gui.doesDisplayPowerBar()){
            if(mouseX >= this.guiLeft+6+192 && mouseY >= this.guiTop+5+3){
                if(mouseX < this.guiLeft+6+192+31 && mouseY < this.guiTop+5+3+132){
                    int current = this.currentData.getEnergyStored();
                    int max = this.currentData.getMaxEnergyStored();
                
                    List<ITextComponent> list = new ArrayList<>();
                    list.add(new StringTextComponent(TextFormatting.GOLD.toString()).append(new TranslationTextComponent(RarmorAPI.MOD_ID + ".storedEnergy")).appendString(" :"));
                    list.add(new StringTextComponent(TextFormatting.YELLOW.toString() + current + "/" + max));
                    list.add(new StringTextComponent(TextFormatting.ITALIC.toString() + (int)(((float)current / (float)max) * 100) + "%"));
                    GuiUtils.drawHoveringText(matrixStack, list, mouseX, mouseY, this.minecraft.getMainWindow().getWidth(), this.minecraft.getMainWindow().getHeight(), -1, this.minecraft.fontRenderer);
                }
            }
        }
        
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y){
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(RES_LOC);
        
        blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
    
        this.gui.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y);
    
        if(this.gui.doesDisplayPowerBar()){
            this.minecraft.getTextureManager().bindTexture(GuiModuleMain.RES_LOC);
            blit(matrixStack, this.guiLeft + 197, this.guiTop + 7, 191, 2, 33, 134);
        
            int i = this.currentData.getEnergyStored()*132/this.currentData.getMaxEnergyStored();
            if(i > 0){
                float[] colors = Helper.getColor(this.minecraft.world.getGameTime() % 256);
                RenderSystem.color3f(colors[0]/255F, colors[1]/255F, colors[2]/255F);
                blit(matrixStack, this.guiLeft + 198, this.guiTop + 140 - i, 224, 134 - i + 1, 31, i);
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
                if(module != null && module.hasTab(this.minecraft.player)){
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
    public void tick(){
        super.tick();
        this.gui.updateScreen();
    }
    
}
