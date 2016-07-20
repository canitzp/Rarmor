package de.canitzp.rarmor.network;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.RarmorValues;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author canitzp
 */
public class PacketHandler {

    public static SimpleNetworkWrapper network;

    public static void init(){
        NetworkRegistry.INSTANCE.registerGuiHandler(Rarmor.instance, new GuiHandler());
        network = NetworkRegistry.INSTANCE.newSimpleChannel(RarmorValues.MODNAME);
        network.registerMessage(PacketOpenGui.Handler.class, PacketOpenGui.class, 0, Side.SERVER);
        network.registerMessage(PacketSetTab.Handler.class, PacketSetTab.class, 1, Side.SERVER);
        network.registerMessage(PacketPaintRarmor.Handler.class, PacketPaintRarmor.class, 2, Side.SERVER);
        network.registerMessage(PacketRarmorPacketData.Handler.class, PacketRarmorPacketData.class, 3, Side.CLIENT);
        network.registerMessage(PacketSendBoolean.class, PacketSendBoolean.class, 4, Side.SERVER);
        network.registerMessage(PacketRequestEnergyLevel.class, PacketRequestEnergyLevel.class, 5, Side.SERVER);
        network.registerMessage(PacketGetEnergy.class, PacketGetEnergy.class, 6, Side.CLIENT);
    }

}
