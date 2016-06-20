package de.canitzp.rarmor.network;

import de.canitzp.rarmor.Rarmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
        network.registerMessage(PacketSetColor.Handler.class, PacketSetColor.class, 2, Side.SERVER);
    }

    public void init(FMLInitializationEvent event){}

    public void registerTexture(ItemStack stack, String name){
        if(!this.textures.keySet().contains(stack)){
            this.textures.put(stack, name);
        }
    }

}
