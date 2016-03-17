package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * @author canitzp
 */
public class ItemModuleStatistics extends ItemModule implements IRarmorModule {

    public ItemModuleStatistics() {
        super("moduleStatistics");
    }

    @Override
    public String getUniqueName() {
        return "statistics";
    }

    @Override
    public void renderWorldScreen(Minecraft minecraft, EntityPlayer player, ScaledResolution resolution, FontRenderer fontRendererObj, RenderGameOverlayEvent.ElementType type, ItemStack module, float partialTicks) {
        fontRendererObj.drawString("Time:" + player.worldObj.getWorldTime() / 20 * 60, 100, 100, ColorUtil.WHITE);
    }
}
