/*
 * This file ("ItemModuleJump.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.jump;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.item.ItemRarmorModule;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemModuleJump extends ItemRarmorModule {

    public ItemModuleJump(String name){
        super(name);
    }

    @Override
    public String[] getModuleIdentifiers(ItemStack stack){
        return new String[]{ActiveModuleJump.IDENTIFIER};
    }

    @Override
    public boolean canInstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData){
        return RarmorAPI.methodHandler.getHasRarmorInSlot(player, EntityEquipmentSlot.FEET) != null;
    }

    @Override
    public boolean canUninstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
        tooltip.add(TextFormatting.ITALIC+I18n.format(RarmorAPI.MOD_ID+".needsShoes"));
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)entity;
            if(!player.isSneaking()){
                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, false);
                if(data != null){
                    int use = 50;
                    if(data.getEnergyStored() >= use){
                        if(data.getInstalledModuleWithId(ActiveModuleJump.IDENTIFIER) != null && RarmorAPI.methodHandler.getHasRarmorInSlot(player, EntityEquipmentSlot.FEET) != null){
                            player.motionY += 0.3;
                            data.extractEnergy(use, false);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)entity;
            if(!player.isSneaking()){
                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, false);
                if(data != null){
                    if(data.getInstalledModuleWithId(ActiveModuleJump.IDENTIFIER) != null && RarmorAPI.methodHandler.getHasRarmorInSlot(player, EntityEquipmentSlot.FEET) != null){
                        event.setDistance(event.getDistance()-2F);
                    }
                }
            }
        }
    }
}
