/*
 * This file 'PlayerEvents.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.event;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.util.NBTUtil;
import de.canitzp.rarmor.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author canitzp
 */
public class PlayerEvents{

    @SubscribeEvent
    public void onPlayerLoggingIn(EntityJoinWorldEvent event){
        if(event.getEntity() instanceof EntityPlayer){
            World world = event.getWorld();
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if(RarmorUtil.isPlayerWearingRarmor(player)){
                ItemStack module = NBTUtil.readSlots(PlayerUtil.getArmor(player, EntityEquipmentSlot.CHEST), ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
                if(module != null && module.getItem() instanceof IRarmorModule){
                    ((IRarmorModule) module.getItem()).onPlayerLoginEvent(world, player, RarmorUtil.getPlayersRarmorChestplate(player), module);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTakeDamage(LivingHurtEvent event){
        if(event.getEntity() instanceof EntityPlayer){
            if(RarmorUtil.isPlayerWearingRarmor((EntityPlayer) event.getEntity())){
                ItemStack module = RarmorUtil.getRarmorModule((EntityPlayer) event.getEntity());
                if(module != null){
                    event.setCanceled(((IRarmorModule)module.getItem()).onPlayerTakeDamage(event.getEntity().getEntityWorld(), (EntityPlayer) event.getEntity(), RarmorUtil.getPlayersRarmorChestplate((EntityPlayer) event.getEntity()), module, event.getSource(), event.getAmount()));
                }
            }
        }
    }

}
