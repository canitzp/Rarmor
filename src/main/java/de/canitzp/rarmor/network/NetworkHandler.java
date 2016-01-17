package de.canitzp.rarmor.network;

import de.canitzp.util.packets.PacketOpenGui;
import de.canitzp.rarmor.Rarmor;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author canitzp
 */
public class NetworkHandler {

    public static SimpleNetworkWrapper wrapper;

    public static void init(){
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Rarmor.MODID);
        wrapper.registerMessage(PacketOpenGui.Handler.class, PacketOpenGui.class, 0, Side.SERVER);
    }

}
