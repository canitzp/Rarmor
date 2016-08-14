/*
 * This file ("TexturedButton.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.inventory.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class TexturedButton extends CustomButton{

    protected final ResourceLocation resLoc;
    protected final int u;
    protected final int v;

    public TexturedButton(int buttonId, int x, int y, int width, int height, ResourceLocation resLoc, int u, int v){
        super(buttonId, x, y, width, height);
        this.resLoc = resLoc;
        this.u = u;
        this.v = v;
    }

    @Override
    protected void drawCustom(Minecraft mc, int mouseX, int mouseY){
        mc.getTextureManager().bindTexture(this.resLoc);

        int actualV = this.v;
        if(this.hovered){
            actualV += this.height;
        }
        this.drawTexturedModalRect(this.xPosition, this.yPosition, this.u, actualV, this.width, this.height);
    }
}
