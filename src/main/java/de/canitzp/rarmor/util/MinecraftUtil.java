/*
 * This file 'MinecraftUtil.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SuppressWarnings("ConstantConditions")
public class MinecraftUtil{

    @SideOnly(Side.CLIENT)
    public static Minecraft getMinecraft(){
        return Minecraft.getMinecraft();
    }

    @SideOnly(Side.CLIENT)
    public static FontRenderer getFontRenderer(){
        return getMinecraft().fontRendererObj;
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer getPlayer(){
        return getMinecraft().thePlayer;
    }

    @SideOnly(Side.CLIENT)
    public static GuiScreen getCurrentScreen(){
        return getMinecraft().currentScreen;
    }

}
