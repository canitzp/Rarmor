package de.canitzp.api.packets;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import de.canitzp.api.CAPI;

/**
 * @author canitzp
 */
public class PacketHandler {

    public static SimpleNetworkWrapper wrapper;
    private static boolean isReg;

    public static void preInit(){
        if(!isReg){
            CAPI.logger.info("Registering CAPI NetworkHandler!");
            wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("CAPI");
            wrapper.registerMessage(PacketOpenGui.Handler.class, PacketOpenGui.class, 0, Side.SERVER);
            isReg = true;
        } else {
            CAPI.logger.error("Tried to many times to register CAPI NetworkHandler!");
        }
    }

    public static boolean isRegistered(){
        return isReg;
    }

}
