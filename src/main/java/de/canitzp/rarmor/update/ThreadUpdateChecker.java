/*
 * This file ("ThreadUpdateChecker.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.update;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.misc.Helper;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

public class ThreadUpdateChecker extends Thread{

    public ThreadUpdateChecker(){
        this.setName(Rarmor.MOD_NAME+" Update Checker");
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run(){
        Rarmor.LOGGER.info("Starting Update Check...");
        try{
            URL newestURL = new URL("https://raw.githubusercontent.com/Ellpeck/Rarmor/master/update/updateVersions.properties");
            Properties updateProperties = new Properties();
            updateProperties.load(new InputStreamReader(newestURL.openStream()));

            String currentMcVersion = Helper.getMcVersion();
            String newestVersionProp = updateProperties.getProperty(currentMcVersion);

            float updateVersionInt = Float.parseFloat(newestVersionProp);
            UpdateChecker.updateVersionString = currentMcVersion+"-r"+newestVersionProp;

            String clientVersionString = Helper.getMajorModVersion();
            float clientVersion = Float.parseFloat(clientVersionString);
            if(updateVersionInt > clientVersion){
                UpdateChecker.needsUpdateNotify = true;
            }

            Rarmor.LOGGER.info("Update Check done!");
        }
        catch(Exception e){
            Rarmor.LOGGER.error("Update Check failed!", e);
            UpdateChecker.checkFailed = true;
        }

        if(!UpdateChecker.checkFailed){
            if(UpdateChecker.needsUpdateNotify){
                Rarmor.LOGGER.info("There is an Update for "+Rarmor.MOD_NAME+" available!");
                Rarmor.LOGGER.info("Current Version: "+Rarmor.VERSION+", newest Version: "+UpdateChecker.updateVersionString+"!");
                Rarmor.LOGGER.info("View the Changelog at "+UpdateChecker.CHANGELOG_LINK);
                Rarmor.LOGGER.info("Download at "+UpdateChecker.DOWNLOAD_LINK);
            }
            else{
                Rarmor.LOGGER.info(Rarmor.MOD_NAME+" is up to date!");
            }
        }
    }
}
