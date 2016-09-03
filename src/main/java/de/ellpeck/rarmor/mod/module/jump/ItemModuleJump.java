/*
 * This file ("ItemModuleJump.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.jump;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.mod.item.ItemRarmorModule;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class ItemModuleJump extends ItemRarmorModule{

    public ItemModuleJump(String name){
        super(name);

        MinecraftForge.EVENT_BUS.register(this);
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

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
        tooltip.add(TextFormatting.ITALIC+I18n.format(RarmorAPI.MOD_ID+".needsShoes"));
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event){
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
    public void onFall(LivingFallEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)entity;
            if(!player.isSneaking()){
                IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, false);
                if(data != null){
                    if(data.getInstalledModuleWithId(ActiveModuleJump.IDENTIFIER) != null && RarmorAPI.methodHandler.getHasRarmorInSlot(player, EntityEquipmentSlot.FEET) != null){
                        event.setDistance(event.getDistance()-1.65F);
                    }
                }
            }
        }
    }
}
