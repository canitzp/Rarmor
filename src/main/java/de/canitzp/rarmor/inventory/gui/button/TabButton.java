/*
 * This file ("TabButton.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.inventory.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.misc.Helper;
import de.canitzp.rarmor.module.main.GuiModuleMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

public class TabButton extends TexturedButton{

    public int moduleNum;
    private IRarmorData data;
    private String moduleName;
    private ItemStack tabIcon = ItemStack.EMPTY;

    public TabButton(int x, int y, Component title, Button.OnPress onPress){
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
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks){
        if(this.tabIcon.isEmpty()){
            return;
        }
        Helper.renderStackToGui(matrixStack, this.tabIcon, this.x+75, this.y+1.5F, 1F);
    
        RenderSystem.setShaderTexture(0, this.resLoc);
    
        int actualV = this.v;
        if(this.moduleNum == this.data.getSelectedModule() || this.isHoveredOrFocused()){
            actualV += this.height;
        }
        blit(matrixStack, this.x, this.y, this.u, actualV, this.width, this.height);
        Minecraft.getInstance().font.draw(matrixStack, I18n.get("module."+this.moduleName+".name"), this.x+8, this.y+6, 0);
    }
    
    @Override
    public void playDownSound(SoundManager handler){
        if(this.moduleNum != this.data.getSelectedModule()){
            super.playDownSound(handler);
        }
    }
    
}
