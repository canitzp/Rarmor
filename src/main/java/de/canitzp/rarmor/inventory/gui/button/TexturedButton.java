/*
 * This file ("TexturedButton.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.inventory.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class TexturedButton extends Button {

    protected final ResourceLocation resLoc;
    public int u;
    public int v;

    public TexturedButton(int x, int y, int width, int height, ResourceLocation resLoc, int u, int v, Component title, Button.OnPress onPress){
        super(x, y, width, height, title, onPress);
        this.resLoc = resLoc;
        this.u = u;
        this.v = v;
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks){
        RenderSystem.setShaderTexture(0, this.resLoc);
        
        int actualV = this.v;
        if(this.isHoveredOrFocused()){
            actualV += this.height;
        }
        blit(matrixStack, this.x, this.y, this.u, actualV, this.width, this.height);
    }
    
}
