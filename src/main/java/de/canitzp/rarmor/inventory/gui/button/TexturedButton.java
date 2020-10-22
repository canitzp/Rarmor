/*
 * This file ("TexturedButton.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.inventory.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TexturedButton extends Button {

    protected final ResourceLocation resLoc;
    public int u;
    public int v;

    public TexturedButton(int x, int y, int width, int height, ResourceLocation resLoc, int u, int v, ITextComponent title, IPressable onPress){
        super(x, y, width, height, title, onPress);
        this.resLoc = resLoc;
        this.u = u;
        this.v = v;
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
        Minecraft.getInstance().getTextureManager().bindTexture(this.resLoc);
        
        int actualV = this.v;
        if(this.isHovered()){
            actualV += this.height;
        }
        blit(matrixStack, this.x, this.y, this.u, actualV, this.width, this.height);
    }
    
}
