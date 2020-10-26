/*
 * This file ("GuiModuleMain.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.main;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.misc.Helper;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiModuleMain extends RarmorModuleGui{

    public static final ResourceLocation RES_LOC = Helper.getGuiLocation("gui_rarmor_main");

    public GuiModuleMain(ActiveRarmorModule module){
        super(module);
    }
    
    @Override
    public void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY){
        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.blit(matrixStack, this.guiLeft+6, this.guiTop+5, 0, 0, 190, 136);
    
        InventoryScreen.drawEntityOnScreen(this.guiLeft+118, this.guiTop+128, 55, (float)(this.guiLeft+118)-mouseX, (float)(this.guiTop+45)-mouseY, this.mc.player);
    }

    @Override
    public boolean doesDisplayPowerBar(){
        return true;
    }
}
