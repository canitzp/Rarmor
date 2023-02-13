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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class ItemModuleJump extends ItemRarmorModule {
    
    @Override
    public String[] getModuleIdentifiers(ItemStack stack){
        return new String[]{ActiveModuleJump.IDENTIFIER};
    }

    @Override
    public boolean canInstall(Player player, Slot slot, ItemStack stack, IRarmorData currentData){
        return RarmorAPI.methodHandler.getHasRarmorInSlot(player, EquipmentSlot.FEET) != null;
    }

    @Override
    public boolean canUninstall(Player player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn){
        tooltip.add(new TextComponent(ChatFormatting.ITALIC.toString()).append(new TranslatableComponent(RarmorAPI.MOD_ID+".needsShoes")));
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player){
            Player player = (Player)entity;
            if(!player.isCrouching()){
                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, false);
                if(data != null){
                    int use = 50;
                    if(data.getEnergyStored() >= use){
                        if(data.getInstalledModuleWithId(ActiveModuleJump.IDENTIFIER) != null && RarmorAPI.methodHandler.getHasRarmorInSlot(player, EquipmentSlot.FEET) != null){
                            player.setDeltaMovement(player.getDeltaMovement().add(0, 0.3, 0));
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
        if(entity instanceof Player){
            Player player = (Player)entity;
            if(!player.isCrouching()){
                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, false);
                if(data != null){
                    if(data.getInstalledModuleWithId(ActiveModuleJump.IDENTIFIER) != null && RarmorAPI.methodHandler.getHasRarmorInSlot(player, EquipmentSlot.FEET) != null){
                        event.setDistance(event.getDistance()-2F);
                    }
                }
            }
        }
    }
}
