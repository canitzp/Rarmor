/*
 * This file ("TabButton.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.inventory.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.misc.Helper;
import de.canitzp.rarmor.module.main.GuiModuleMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class TabButton extends TexturedButton{

    public int moduleNum;
    private IRarmorData data;
    private String moduleName;
    private ItemStack tabIcon;

    public TabButton(int x, int y, ITextComponent title, IPressable onPress){
        super(x, y, 94, 20, GuiModuleMain.RES_LOC, 20, 216, title, onPress);
    }

    public void setModule(IRarmorData data, int selectedModule){
        this.moduleNum = selectedModule;
        this.data = data;

        ActiveRarmorModule module = data.getCurrentModules().get(selectedModule);
        this.moduleName = module.getIdentifier();
        this.tabIcon = module.getDisplayIcon();
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
        Helper.renderStackToGui(this.tabIcon, this.x+75, this.y+1.5F, 1F);
    
        Minecraft.getInstance().getTextureManager().bindTexture(this.resLoc);
    
        int actualV = this.v;
        if(this.moduleNum == this.data.getSelectedModule() || this.isHovered()){
            actualV += this.height;
        }
        blit(matrixStack, this.x, this.y, this.u, actualV, this.width, this.height);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, I18n.format("module."+this.moduleName+".name"), this.x+8, this.y+6, 0);
    }
    
    @Override
    public void playDownSound(SoundHandler handler){
        if(this.moduleNum != this.data.getSelectedModule()){
            super.playDownSound(handler);
        }
    }
    
}
