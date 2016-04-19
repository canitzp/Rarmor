package de.canitzp.rarmor.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SuppressWarnings("ConstantConditions")
public class MinecraftUtil {

    public static Side getMinecraftSide() {
        try {
            ClassLoader.getSystemClassLoader().loadClass("net.minecraft.client.main.Main");
            return Side.CLIENT;
        } catch (ClassNotFoundException e) {
            return Side.SERVER;
        }
    }

    @SideOnly(Side.CLIENT)
    public static Minecraft getMinecraft() {
        if (getMinecraftSide().isClient()) {
            return Minecraft.getMinecraft();
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static FontRenderer getFontRenderer() {
        if (getMinecraftSide().isClient()) {
            return getMinecraft().fontRendererObj;
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer getPlayer() {
        if (getMinecraftSide().isClient()) {
            return getMinecraft().thePlayer;
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static GuiScreen getCurrentScreen() {
        if (MinecraftUtil.getMinecraftSide().isClient()) {
            return getMinecraft().currentScreen;
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static RayTraceResult getMouseOver() {
        if (getMinecraftSide().isClient()) {
            return getMinecraft().objectMouseOver;
        }
        return null;
    }

}
