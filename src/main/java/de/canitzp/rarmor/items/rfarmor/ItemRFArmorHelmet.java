package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.util.util.ColorUtil;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.MovingObjectPosition;

/**
 * @author canitzp
 */
public class ItemRFArmorHelmet extends ItemRFArmorGeneric{

    public ItemRFArmorHelmet() {
        super(ArmorType.HEAD, 250000, 1500, "rfArmorHelmet");
        setCreativeTab(Rarmor.rarmorTab);
    }

    public void displayHud(Minecraft minecraft, EntityPlayer player, ScaledResolution resolution) {
        ItemStack stack = player.getCurrentArmor(2);
        if(stack != null && NBTUtil.getBoolean(stack, "SettingInWorldTooltip"))
            ArmorHud.displayNames(minecraft, resolution, player, 0, 5, ColorUtil.WHITE);
    }
}
