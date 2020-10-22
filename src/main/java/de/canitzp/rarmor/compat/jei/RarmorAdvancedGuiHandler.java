/*
 * This file ("RarmorAdvancedGuiHandler.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.compat.jei;

import de.canitzp.rarmor.inventory.gui.GuiRarmor;
import de.canitzp.rarmor.inventory.gui.button.TabButton;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.gui.handlers.IScreenHandler;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.renderer.Rectangle2d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RarmorAdvancedGuiHandler implements IGuiContainerHandler<GuiRarmor> {

    @Nonnull
    @Override
    public Class<GuiRarmor> getGuiContainerClass(){
        return GuiRarmor.class;
    }

    @Nullable
    @Override
    public List<Rectangle> getGuiExtraAreas(GuiRarmor gui){
        List<Rectangle> list = new ArrayList<Rectangle>();

        for(TabButton button : gui.tabButtons){
            if(button.visible){
                list.add(new Rectangle(button.x, button.y, button.width, button.height));
            }
        }

        return list;
    }

    @Nullable
    @Override
    public Object getIngredientUnderMouse(GuiRarmor guiContainer, int mouseX, int mouseY) {
        return null;
    }
    
}
