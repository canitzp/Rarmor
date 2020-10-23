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
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
    public boolean canInstall(PlayerEntity player, Slot slot, ItemStack stack, IRarmorData currentData){
        return RarmorAPI.methodHandler.getHasRarmorInSlot(player, EquipmentSlotType.FEET) != null;
    }

    @Override
    public boolean canUninstall(PlayerEntity player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
        tooltip.add(new StringTextComponent(TextFormatting.ITALIC.toString()).append(new TranslationTextComponent(RarmorAPI.MOD_ID+".needsShoes")));
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity)entity;
            if(!player.isSneaking()){
                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, false);
                if(data != null){
                    int use = 50;
                    if(data.getEnergyStored() >= use){
                        if(data.getInstalledModuleWithId(ActiveModuleJump.IDENTIFIER) != null && RarmorAPI.methodHandler.getHasRarmorInSlot(player, EquipmentSlotType.FEET) != null){
                            player.setMotion(player.getMotion().add(0, 0.3, 0));
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
        if(entity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity)entity;
            if(!player.isSneaking()){
                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, false);
                if(data != null){
                    if(data.getInstalledModuleWithId(ActiveModuleJump.IDENTIFIER) != null && RarmorAPI.methodHandler.getHasRarmorInSlot(player, EquipmentSlotType.FEET) != null){
                        event.setDistance(event.getDistance()-2F);
                    }
                }
            }
        }
    }
}
