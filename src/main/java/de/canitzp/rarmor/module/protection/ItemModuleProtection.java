/*
 * This file ("ItemModuleProtection.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.protection;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.item.ItemRarmorModule;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemModuleProtection extends ItemRarmorModule{

    private final String identifier;
    private static boolean eventRegistered;

    public ItemModuleProtection(String identifier){
        this.identifier = identifier;

        if(!eventRegistered){
            MinecraftForge.EVENT_BUS.register(this);
            eventRegistered = true;
        }
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event){
        LivingEntity entity = event.getEntityLiving();
        if(!entity.getLevel().isClientSide() && entity instanceof Player){
            Player player = (Player)entity;
            IRarmorData data = RarmorAPI.methodHandler.getDataForChestplate(player, false);
            if(data != null){
                float damageReduct = 0F;

                for(ActiveRarmorModule module : data.getCurrentModules()){
                    if(module instanceof ActiveModuleProtection){
                        damageReduct = ((ActiveModuleProtection)module).getDamageReduction();
                        break;
                    }
                }

                if(damageReduct > 0F){
                    int neededEnergy = (int)(damageReduct*200);

                    if(data.getEnergyStored() >= neededEnergy){
                        event.setAmount(Math.max(0, event.getAmount()/damageReduct));
                        data.extractEnergy(neededEnergy, false);
                    }
                }
            }
        }
    }

    @Override
    public String[] getModuleIdentifiers(ItemStack stack){
        return new String[]{this.identifier};
    }

    @Override
    public boolean canInstall(Player player, Slot slot, ItemStack stack, IRarmorData currentData){
        for(ActiveRarmorModule module : currentData.getCurrentModules()){
            if(module instanceof ActiveModuleProtection){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canUninstall(Player player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }
}
