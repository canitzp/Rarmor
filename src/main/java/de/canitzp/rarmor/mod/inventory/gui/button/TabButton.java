/*
 * This file ("TabButton.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.inventory.gui.button;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.mod.misc.Helper;
import de.canitzp.rarmor.mod.module.main.GuiModuleMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class TabButton extends TexturedButton{

    private IRarmorData data;
    private String moduleName;
    private ItemStack tabIcon;
    public int moduleNum;

    public TabButton(int buttonId, int x, int y){
        super(buttonId, x, y, 94, 20, GuiModuleMain.RES_LOC, 20, 216);
    }

    public void setModule(IRarmorData data, int selectedModule){
        this.moduleNum = selectedModule;
        this.data = data;

        ActiveRarmorModule module = data.getCurrentModules().get(selectedModule);
        this.moduleName = module.getIdentifier();
        this.tabIcon = module.getTabIcon();
    }

    @Override
    protected void drawCustom(Minecraft mc, int mouseX, int mouseY){
        Helper.renderStackToGui(this.tabIcon, this.xPosition+75, this.yPosition+1.5F, 1F);

        mc.getTextureManager().bindTexture(this.resLoc);

        int actualV = this.v;
        if(this.moduleNum == this.data.getSelectedModule() || this.hovered){
            actualV += this.height;
        }
        this.drawTexturedModalRect(this.xPosition, this.yPosition, this.u, actualV, this.width, this.height);
        mc.fontRendererObj.drawString(I18n.format("module."+this.moduleName+".name"), this.xPosition+8, this.yPosition+6, 0);
    }

    @Override
    public void playPressSound(SoundHandler handler){
        if(this.moduleNum != this.data.getSelectedModule()){
            super.playPressSound(handler);
        }
    }
}
