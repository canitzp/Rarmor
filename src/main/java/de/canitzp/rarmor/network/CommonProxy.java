package de.canitzp.rarmor.network;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author canitzp
 */
public class CommonProxy{

    protected Map<ItemStack, String> textures = new HashMap<>();


    public void preInit(FMLPreInitializationEvent event){


        MinecraftForge.EVENT_BUS.register(this);
    }

    public void init(FMLInitializationEvent event){}

    public void registerTexture(ItemStack stack, String name){
        if(!this.textures.keySet().contains(stack)){
            this.textures.put(stack, name);
        }
    }

    @SubscribeEvent
    public void playerLoggout(PlayerEvent.PlayerLoggedOutEvent event){
        saveRarmor(event.player);
    }

    @SubscribeEvent
    public void saveWorld(WorldEvent.Save event){
        for(EntityPlayer player : event.getWorld().playerEntities){
            saveRarmor(player);
        }
    }

    @SubscribeEvent
    public void unloadWorld(WorldEvent.Unload event){
        for(EntityPlayer player : event.getWorld().playerEntities){
            saveRarmor(player);
        }
    }

    public void saveRarmor(EntityPlayer player){
        if(!player.worldObj.isRemote){
            if(RarmorUtil.isPlayerWearingArmor(player)){
                ItemStack stack = RarmorUtil.getRarmorChestplate(player);
                if(RarmorAPI.hasRarmorTabs(player.getEntityWorld(), stack)){
                    for(IRarmorTab tab : RarmorAPI.getTabsFromStack(player.getEntityWorld(), stack)){
                        NBTTagCompound tabNBT = NBTUtil.getTagFromStack(stack).getCompoundTag(tab.getTabIdentifier(stack, player));
                        tab.writeToNBT(tabNBT);
                    }
                }
                UUID uuid = NBTUtil.getTagFromStack(stack).getUniqueId("RarmorUUID");
                RarmorAPI.tabListServer.remove(uuid);
                NBTUtil.getTagFromStack(stack).removeTag("RarmorUUID");
            }
        }
    }

}
