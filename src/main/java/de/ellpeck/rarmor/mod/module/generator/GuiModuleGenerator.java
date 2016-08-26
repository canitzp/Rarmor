/*
 * This file ("GuiModuleGenerator.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.generator;

import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.misc.Helper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiModuleGenerator extends RarmorModuleGui{

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("guiRarmorGenerator");

    public GuiModuleGenerator(GuiContainer gui, ActiveRarmorModule module){
        super(gui, module);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.drawTexturedModalRect(this.guiLeft+109, this.guiTop+48, 0, 0, 18, 34);

        ActiveModuleGenerator gen = (ActiveModuleGenerator)this.module;
        if(gen.currentBurnTime > 0){
            int i = gen.currentBurnTime*13/gen.burnTimeTickingDownFrom;
            this.drawTexturedModalRect(this.guiLeft+110, this.guiTop+48+12-i, 176, 12-i, 14, i+1);
        }
    }

    @Override
    public boolean doesDisplayPowerBar(){
        return true;
    }
}
