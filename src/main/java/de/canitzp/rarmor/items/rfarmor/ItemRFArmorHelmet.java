package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IIngameTooltipHandler;
import de.canitzp.rarmor.api.RamorResources;
import de.canitzp.util.util.ColorUtil;
import de.canitzp.util.util.GuiUtil;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class ItemRFArmorHelmet extends ItemRFArmorGeneric implements IIngameTooltipHandler{

    private ResourceLocation batLoc;

    public ItemRFArmorHelmet() {
        super(ArmorType.HEAD, 250000, 1500, "rfArmorHelmet");
        batLoc = RamorResources.BATTERYGUI.getNewLocation();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void doRender(Minecraft minecraft, EntityPlayer playerSP, ScaledResolution resolution, FontRenderer fontRenderer, RenderGameOverlayEvent.ElementType elementType, ItemStack helmet, float partialTicks) {
        if(RarmorUtil.isPlayerWearingRarmor(playerSP) && NBTUtil.getBoolean(playerSP.getCurrentArmor(2), "SettingInWorldTooltip")){
            ArmorHud.displayNames(minecraft, resolution, playerSP, 0, 5, ColorUtil.WHITE);
            //drawEnergyBarVer(100, 100, playerSP.getCurrentArmor(2).getMaxDamage(), NBTUtil.getInteger(playerSP.getCurrentArmor(2), "Energy"));
        }
    }

    public void drawEnergyBarVer(int x, int y, int maxCap, int amount){
        GuiUtil.bindTexture(batLoc);
        float height = 21;
        float factor = amount * height / maxCap;
        GuiUtil.drawTexturedModalRect(x, y, 0, 0, 10, (int) height);
        GuiUtil.drawTexturedModalRect(x, y, 0, (int)height, (int)factor + 1, (int) (factor/height * height));
    }

}
