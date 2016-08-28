/*
 * This file ("UpdateChecker.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.update;

import de.ellpeck.rarmor.mod.Rarmor;
import de.ellpeck.rarmor.mod.config.Config;
import de.ellpeck.rarmor.mod.misc.Helper;

public final class UpdateChecker{

    public static final String DOWNLOAD_LINK = "http://ellpeck.de/rarmordownload";
    public static final String CHANGELOG_LINK = "http://ellpeck.de/rarmorchangelog";
    public static boolean checkFailed;
    public static boolean needsUpdateNotify;
    public static boolean notifiedAlready;
    public static String updateVersionString;

    public UpdateChecker(){
        if(Config.doUpdateCheck && !Helper.isDevVersion()){
            Rarmor.LOGGER.info("Initializing Update Checker...");

            new ThreadUpdateChecker();
        }
    }
}
