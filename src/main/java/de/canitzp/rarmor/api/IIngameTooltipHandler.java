package de.canitzp.rarmor.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public interface IIngameTooltipHandler {

    @SideOnly(Side.CLIENT)
    void doRender(Minecraft minecraft, EntityPlayerSP playerSP, ScaledResolution resolution, FontRenderer fontRenderer, RenderGameOverlayEvent.ElementType elementType, ItemStack helmet, float partialTicks);

}
