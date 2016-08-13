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
import de.canitzp.rarmor.mod.module.main.GuiModuleMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class TabButton extends TexturedButton{

    private IRarmorData data;
    private String moduleName;
    public int moduleNum;

    public TabButton(int buttonId, int x, int y){
        super(buttonId, x, y, 94, 28, GuiModuleMain.RES_LOC, 20, 200);
    }

    public void setModule(IRarmorData data, int selectedModule){
        this.moduleNum = selectedModule;
        this.moduleName = data.getCurrentModules().get(selectedModule).getIdentifier();
        this.data = data;
    }

    @Override
    protected void drawCustom(Minecraft mc, int mouseX, int mouseY){
        mc.getTextureManager().bindTexture(this.resLoc);

        int actualV = this.v;
        if(this.moduleNum == this.data.getSelectedModule()){
            actualV += this.height;
        }
        this.drawTexturedModalRect(this.xPosition, this.yPosition, this.u, actualV, this.width, this.height);
        mc.fontRendererObj.drawString(I18n.format("module."+this.moduleName+".name"), this.xPosition+5, this.yPosition+9, 4210752);
    }
}
