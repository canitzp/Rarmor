/*
 * This file ("Config.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.misc;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class Config{

    public static boolean doOpeningConfirmationPacket;

    public static void init(File file){
        Configuration config = new Configuration(file);
        config.load();

        doOpeningConfirmationPacket = config.get(Configuration.CATEGORY_GENERAL, "openingConfirmation", true, "Turn this off to disable the packet that gets sent from the client back to the server to ensure that it has gotten all of the data a Rarmor contains before opening its GUI. Turning this off might reduce server load.").getBoolean();

        if(config.hasChanged()){
            config.save();
        }
    }

}
