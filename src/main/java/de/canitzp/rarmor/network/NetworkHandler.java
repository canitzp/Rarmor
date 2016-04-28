/*
 * This file 'NetworkHandler.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.network;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.inventory.GuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author canitzp
 */
public class NetworkHandler{

    public static SimpleNetworkWrapper wrapper;

    public static void init(){
        Rarmor.logger.info("Register Network Stuff");
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Rarmor.MODID);
        wrapper.registerMessage(PacketOpenGui.Handler.class, PacketOpenGui.class, 0, Side.SERVER);
        wrapper.registerMessage(PacketSendNBTBoolean.PacketHandler.class, PacketSendNBTBoolean.class, 1, Side.SERVER);
        wrapper.registerMessage(PacketSyncPlayerHotbar.Handler.class, PacketSyncPlayerHotbar.class, 2, Side.SERVER);
        NetworkRegistry.INSTANCE.registerGuiHandler(Rarmor.MODID, new GuiHandler());
    }

}
