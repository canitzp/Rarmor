package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.IIngameTooltipHandler;
import de.canitzp.util.util.ColorUtil;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class ItemRFArmorHelmet extends ItemRFArmorGeneric implements IIngameTooltipHandler{

    public ItemRFArmorHelmet() {
        super(ArmorType.HEAD, 250000, 1500, "rfArmorHelmet");
        setCreativeTab(Rarmor.rarmorTab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void doRender(Minecraft minecraft, EntityPlayerSP playerSP, ScaledResolution resolution, FontRenderer fontRenderer, RenderGameOverlayEvent.ElementType elementType, ItemStack helmet, float partialTicks) {
        if(isRarmorEquipped(playerSP) && NBTUtil.getBoolean(playerSP.getCurrentArmor(2), "SettingInWorldTooltip")){
            ArmorHud.displayNames(minecraft, resolution, playerSP, 0, 5, ColorUtil.WHITE);
        }
    }


    private boolean isRarmorEquipped(EntityPlayer player) {
        ItemStack head = player.getCurrentArmor(3);
        ItemStack body = player.getCurrentArmor(2);
        ItemStack leggins = player.getCurrentArmor(1);
        ItemStack boots = player.getCurrentArmor(0);
        if (head != null && body != null && leggins != null && boots != null) {
            if (head.getItem() instanceof ItemRFArmorGeneric && body.getItem() instanceof ItemRFArmorBody && leggins.getItem() instanceof ItemRFArmorGeneric && boots.getItem() instanceof ItemRFArmorGeneric) {
                return true;
            }
        }
        return false;
    }
}
