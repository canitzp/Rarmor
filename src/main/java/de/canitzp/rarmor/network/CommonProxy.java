package de.canitzp.rarmor.network;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class CommonProxy{

    protected Map<ItemStack, String> textures = new HashMap<>();

    public SimpleNetworkWrapper network;

    public void preInit(FMLPreInitializationEvent event){
        NetworkRegistry.INSTANCE.registerGuiHandler(Rarmor.instance, new GuiHandler());
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Rarmor.NAME);
        network.registerMessage(PacketOpenGui.Handler.class, PacketOpenGui.class, 0, Side.SERVER);
        network.registerMessage(PacketSetTab.Handler.class, PacketSetTab.class, 1, Side.SERVER);
        network.registerMessage(PacketPaintRarmor.Handler.class, PacketPaintRarmor.class, 2, Side.SERVER);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void init(FMLInitializationEvent event){}

    public void registerTexture(ItemStack stack, String name){
        if(!this.textures.keySet().contains(stack)){
            this.textures.put(stack, name);
        }
    }

    @SubscribeEvent
    public void saveWorld(PlayerEvent.PlayerLoggedOutEvent event){
        EntityPlayer player = event.player;
        World world = player.worldObj;
        if(!world.isRemote){
            if(RarmorUtil.isPlayerWearingArmor(player)){
                ItemStack stack = RarmorUtil.getRarmorChestplate(player);
                for(IRarmorTab tab : RarmorAPI.tickMap.get(stack)){
                    tab.writeToNBT(NBTUtil.getTagFromStack(stack).getCompoundTag(tab.getTabIdentifier(stack, player)));
                }
                System.out.println("saved");
            }
        }
    }

}
