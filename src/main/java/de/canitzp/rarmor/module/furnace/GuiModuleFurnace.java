/*
 * This file ("GuiModuleFurnace.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.furnace;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiModuleFurnace extends RarmorModuleGui {

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("gui_rarmor_furnace");

    public GuiModuleFurnace(ActiveRarmorModule module){
        super(module);
    }
    
    @Override
    public void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY){
        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.blit(matrixStack, this.guiLeft+81, this.guiTop+53, 0, 0, 72, 26);
    
        int i = this.getCookProgressScaled(24);
        this.blit(matrixStack, this.guiLeft+81+20, this.guiTop+53+4, 176, 14, i+1, 16);
    }

    private int getCookProgressScaled(int pixels){
        return ((ActiveModuleFurnace)this.module).burnTime*pixels/ActiveModuleFurnace.TIME_TO_REACH;
    }

    @Override
    public boolean doesDisplayPowerBar(){
        return true;
    }
}
