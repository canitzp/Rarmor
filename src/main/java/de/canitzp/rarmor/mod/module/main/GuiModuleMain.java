/*
 * This file ("GuiModuleMain.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.module.main;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.IActiveRarmorModule;
import de.canitzp.rarmor.mod.event.ClientEvents;
import de.canitzp.rarmor.mod.inventory.gui.button.TexturedButton;
import de.canitzp.rarmor.mod.misc.Helper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Collections;

@SideOnly(Side.CLIENT)
public class GuiModuleMain extends RarmorModuleGui{

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("guiRarmorMain");

    private GuiButton buttonBackToMainInventory;

    private float oldMouseX;
    private float oldMouseY;

    public GuiModuleMain(GuiContainer container, IActiveRarmorModule module){
        super(container, module);
    }

    @Override
    public void initGui(){
        this.buttonBackToMainInventory = new TexturedButton(0, this.guiLeft+5, this.guiTop+120, 20, 20, RES_LOC, 0, 216);
        this.buttonList.add(this.buttonBackToMainInventory);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.drawTexturedModalRect(this.guiLeft+6, this.guiTop+5, 0, 0, 217, 136);

        GuiInventory.drawEntityOnScreen(this.guiLeft+118, this.guiTop+128, 55, (float)(this.guiLeft+118)-this.oldMouseX, (float)(this.guiTop+45)-this.oldMouseY, this.mc.thePlayer);
        this.oldMouseX = (float)mouseX;
        this.oldMouseY = (float)mouseY;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        if(this.buttonBackToMainInventory.isMouseOver()){
            GuiUtils.drawHoveringText(Collections.singletonList(I18n.format(RarmorAPI.MOD_ID+".back")), mouseX, mouseY, this.mc.displayWidth, this.mc.displayHeight, -1, this.mc.fontRendererObj);
        }

        Slot slot = this.actualGui.getSlotUnderMouse();
        if(slot != null && slot.getSlotIndex() == 38){ //chestplate slot
            GuiUtils.drawHoveringText(Collections.singletonList(TextFormatting.GOLD+""+TextFormatting.ITALIC+I18n.format(RarmorAPI.MOD_ID+".removeChest", this.mc.gameSettings.keyBindDrop.getDisplayName())), mouseX, mouseY-20, this.mc.displayWidth, this.mc.displayHeight, -1, this.mc.fontRendererObj);
        }
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException{
        if(button.id == 0){
            ClientEvents.stopGuiOverride = true;
            this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
            ClientEvents.stopGuiOverride = false;
        }
    }
}
