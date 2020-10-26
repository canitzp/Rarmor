/*
 * This file ("GuiModuleEnder.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.ender;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiModuleEnder extends RarmorModuleGui{

    private static final ResourceLocation RES_LOC = Helper.getGuiLocation("gui_rarmor_ender");

    public GuiModuleEnder(ActiveRarmorModule module){
        super(module);
    }
    
    @Override
    public void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY){
        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.blit(matrixStack, this.guiLeft+37, this.guiTop+25, 0, 0, 162, 54);
    }
    
}
