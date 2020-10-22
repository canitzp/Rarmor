/*
 * This file ("PacketHandler.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.packet;

import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class PacketHandler{

    public static SimpleChannel channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(RarmorAPI.MOD_ID, "network"), () -> "1.0.0", s -> true, s -> true);

    public static void init(){
        channel.registerMessage(0, PacketOpenModule.class, PacketOpenModule::toBytes, PacketOpenModule::fromBuffer, PacketOpenModule::handle); // Server side

        handler.registerMessage(PacketSyncRarmorData.Handler.class, PacketSyncRarmorData.class, 1, Side.CLIENT);
        handler.registerMessage(PacketOpenConfirmation.Handler.class, PacketOpenConfirmation.class, 2, Side.SERVER);
        handler.registerMessage(PacketSyncColor.class, PacketSyncColor.class, 3, Side.SERVER);
    }

}
