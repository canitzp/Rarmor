/*
 * This file 'GuiModularTool.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.inventory.gui;

import de.canitzp.rarmor.api.RarmorResources;
import de.canitzp.rarmor.util.GuiUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author canitzp
 */
public class GuiModularTool extends GuiContainer{

    private ResourceLocation guiLocation = RarmorResources.MODULARTOOLGUI.getNewLocation();

    public GuiModularTool(Container inventorySlotsIn){
        super(inventorySlotsIn);
        this.xSize = 176;
        this.ySize = 111;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiUtil.bindTexture(this.guiLocation);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
