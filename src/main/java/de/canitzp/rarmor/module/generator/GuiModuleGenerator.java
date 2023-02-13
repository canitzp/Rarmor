/*
 * This file ("GuiModuleGenerator.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.generator;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.resources.ResourceLocation;

public class GuiModuleGenerator extends RarmorModuleGui{

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("gui_rarmor_generator");

    public GuiModuleGenerator(ActiveRarmorModule module){
        super(module);
    }
    
    @Override
    public void drawGuiContainerBackgroundLayer(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY){
        RenderSystem.setShaderTexture(0, RES_LOC);
        this.blit(matrixStack, this.guiLeft+109, this.guiTop+48, 0, 0, 18, 34);
    
        ActiveModuleGenerator gen = (ActiveModuleGenerator)this.module;
        if(gen.currentBurnTime > 0 && gen.burnTimeTickingDownFrom > 0){
            int i = gen.currentBurnTime*13/gen.burnTimeTickingDownFrom;
            this.blit(matrixStack, this.guiLeft+110, this.guiTop+48+12-i, 176, 12-i, 14, i+1);
        }
    }

    @Override
    public boolean doesDisplayPowerBar(){
        return true;
    }
}
