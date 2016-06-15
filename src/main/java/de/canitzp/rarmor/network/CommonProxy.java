package de.canitzp.rarmor.network;

import de.canitzp.rarmor.Rarmor;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author canitzp
 */
public class CommonProxy{

    public SimpleNetworkWrapper network;

    public void preInit(FMLPreInitializationEvent event){
        NetworkRegistry.INSTANCE.registerGuiHandler(Rarmor.instance, new GuiHandler());
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Rarmor.NAME);
        network.registerMessage(PacketOpenGui.Handler.class, PacketOpenGui.class, 0, Side.SERVER);
    }

}
