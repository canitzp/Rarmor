package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IIngameTooltipHandler;
import de.canitzp.rarmor.util.NBTUtil;
import de.canitzp.rarmor.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class ItemRFArmorHelmet extends ItemRFArmorGeneric implements IIngameTooltipHandler{

    public ItemRFArmorHelmet() {
        super(EntityEquipmentSlot.HEAD, 250000, 1500, "rfArmorHelmet");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void doRender(Minecraft minecraft, EntityPlayer player, ScaledResolution resolution, FontRenderer fontRenderer, RenderGameOverlayEvent.ElementType elementType, ItemStack helmet, float partialTicks) {
        if(RarmorUtil.isPlayerWearingRarmor(player) && NBTUtil.getBoolean(PlayerUtil.getArmor(player, EntityEquipmentSlot.CHEST), "SettingInWorldTooltip")){
            ArmorHud.display(minecraft, resolution, player, 0, 5);
        }
    }

}
