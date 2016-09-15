/*
 * This file ("ItemModuleProtection.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.protection;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.item.ItemRarmorModule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemModuleProtection extends ItemRarmorModule{

    private final String identifier;
    private static boolean eventRegistered;

    public ItemModuleProtection(String name, String identifier){
        super(name);
        this.identifier = identifier;

        if(!eventRegistered){
            MinecraftForge.EVENT_BUS.register(this);
            eventRegistered = true;
        }
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event){
        EntityLivingBase entity = event.getEntityLiving();
        if(!entity.worldObj.isRemote && entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)entity;
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
    public boolean canInstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData){
        for(ActiveRarmorModule module : currentData.getCurrentModules()){
            if(module instanceof ActiveModuleProtection){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canUninstall(EntityPlayer player, Slot slot, ItemStack stack, IRarmorData currentData){
        return true;
    }
}
