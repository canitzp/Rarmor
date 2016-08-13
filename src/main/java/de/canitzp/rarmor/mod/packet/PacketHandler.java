/*
 * This file ("PacketHandler.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.packet;

import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class PacketHandler{

    public static SimpleNetworkWrapper handler;

    public static void init(){
        handler = new SimpleNetworkWrapper(RarmorAPI.MOD_ID+"network");

        handler.registerMessage(PacketOpenGui.Handler.class, PacketOpenGui.class, 0, Side.SERVER);
    }

}
