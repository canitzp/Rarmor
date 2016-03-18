package de.canitzp.rarmor.event;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author canitzp
 */
public class PlayerEvents {

    @SubscribeEvent
    public void onPlayerLoggingIn(EntityJoinWorldEvent event){
        if(event.entity instanceof EntityPlayer){
            World world = event.world;
            EntityPlayer player = (EntityPlayer) event.entity;
            if(RarmorUtil.isPlayerWearingRarmor(player)){
                ItemStack module = NBTUtil.readSlots(player.inventory.armorInventory[2], ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
                if(module != null && module.getItem() instanceof IRarmorModule){
                    ((IRarmorModule) module.getItem()).onPlayerLoginEvent(world, player, module);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTakeDamage(LivingHurtEvent event){
        if(event.entity instanceof EntityPlayer){
            if(RarmorUtil.isPlayerWearingRarmor((EntityPlayer) event.entity)){
                IRarmorModule module = RarmorUtil.getRarmorModule((EntityPlayer) event.entity);
                if(module != null){
                    event.setCanceled(module.onPlayerTakeDamage(event.entity.getEntityWorld(),(EntityPlayer) event.entity, RarmorUtil.getPlayersRarmorChestplate((EntityPlayer) event.entity), event.source, event.ammount));
                }
            }
        }

    }

}
