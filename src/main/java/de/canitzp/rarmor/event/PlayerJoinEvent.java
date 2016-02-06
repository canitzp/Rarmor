package de.canitzp.rarmor.event;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author canitzp
 */
public class PlayerJoinEvent {

    @SubscribeEvent
    public void onPlayerLoggingIn(EntityJoinWorldEvent event){
        if(event.entity instanceof EntityPlayer){
            World world = event.world;
            EntityPlayer player = (EntityPlayer) event.entity;
            if(RarmorUtil.isPlayerWearingRarmor(player)){
                ItemStack module = NBTUtil.readSlots(player.getCurrentArmor(2), ItemRFArmorBody.slotAmount).getStackInSlot(ItemRFArmorBody.MODULESLOT);
                if(module != null && module.getItem() instanceof IRarmorModule){
                    ((IRarmorModule) module.getItem()).onPlayerLoginEvent(world, player, module);
                }
            }
        }
    }

}
