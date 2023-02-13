/*
 * This file ("RarmorModuleGui.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.api.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.List;

/**
 * A basic GUI class for a Rarmor module that should have a tab that opens a GUI and Container
 * <p>
 * This is similar to Minecraft's GuiContainer as it overrides some of its methods, however, the actual gui
 * this is contained inside of can be accessed.
 * <p>
 * This extends Gui for convenience purposes.
 */
@OnlyIn(Dist.CLIENT)
public class RarmorModuleGui extends Screen {

    public final IRarmorData currentData;
    public final ActiveRarmorModule module;
    public Minecraft mc;

    public int guiLeft;
    public int guiTop;

    public RarmorModuleGui(ActiveRarmorModule module){
        super(new TextComponent(""));
        this.module = module;
        this.currentData = module.data;
    }

    public void initGui(){

    }

    public void updateScreen(){

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks){

    }

    public void drawGuiContainerBackgroundLayer(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY){

    }

    /**
     * Gets if the Rarmor base GUI's power bar should be displayed in this GUI as well.
     * @return If this should display the power bar
     */
    public boolean doesDisplayPowerBar(){
        return false;
    }
}
