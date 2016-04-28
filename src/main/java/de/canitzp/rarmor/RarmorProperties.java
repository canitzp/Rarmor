/*
 * This file 'RarmorProperties.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author canitzp
 */
public class RarmorProperties extends Properties{

    public static RarmorProperties rarmorProperties;
    private static File propFile;
    private static Map<String, Boolean> booleanProperties = new HashMap<>();
    private static Map<String, Integer> integerProperties = new HashMap<>();
    private static Map<String, String[]> stringArrayProperties = new HashMap<>();

    public RarmorProperties(File suggestedConfigurationFile){
        RarmorProperties.propFile = new File(suggestedConfigurationFile.getParent() + File.separator + "Rarmor.properties");
    }

    public static void preInit(FMLPreInitializationEvent event){
        Rarmor.logger.info("Initialize Configuration");
        rarmorProperties = new RarmorProperties(event.getSuggestedConfigurationFile());
        rarmorProperties.readProperties();
    }

    public static boolean getBoolean(String key){
        return booleanProperties.containsKey(key) && booleanProperties.get(key);
    }

    public static int getInteger(String key){
        return integerProperties.containsKey(key) ? integerProperties.get(key) : 0;
    }

    public static String[] getStringArray(String key){
        return stringArrayProperties.containsKey(key) ? stringArrayProperties.get(key) : new String[0];
    }

    @Override
    public synchronized Enumeration<Object> keys(){
        return Collections.enumeration(new TreeSet<>(super.keySet()));
    }

    public void saveProperties(){
        try{
            for(Map.Entry<String, Boolean> entry : booleanProperties.entrySet()){
                rarmorProperties.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            for(Map.Entry<String, Integer> entry : integerProperties.entrySet()){
                rarmorProperties.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            for(Map.Entry<String, String[]> entry : stringArrayProperties.entrySet()){
                rarmorProperties.put(entry.getKey(), Arrays.toString(entry.getValue()));
            }
            rarmorProperties.store(new FileOutputStream(propFile), "These are the Properties of Rarmor");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void readProperties(){
        try{
            rarmorProperties.load(new FileInputStream(propFile));
            for(Map.Entry<Object, Object> entry : entrySet()){
                if(this.isEntryBoolean((String) entry.getValue())){
                    booleanProperties.put((String) entry.getKey(), parseBool((String) entry.getValue()));
                }
                if(this.isEntryInteger((String) entry.getValue())){
                    integerProperties.put((String) entry.getKey(), Integer.parseInt((String) entry.getValue()));
                }
                if(this.isEntryStringArray((String) entry.getValue())){
                    stringArrayProperties.put((String) entry.getKey(), ((String) entry.getValue()).replace("[", "").replace("]", "").replace(" ", "").split(","));
                }
            }
        } catch(IOException e){
            Rarmor.logger.info("Can't find Rarmor.properties creating new ones.");
        }
        initProperties();
    }

    private void initProperties(){
        addInteger("maxRarmorTransferPerTick", 20);
        addInteger("moduleDefenseDamageMultiplier", 2500);
        addInteger("moduleFlyingEnergyPerTick", 5);
        addInteger("moduleSolarEnergyPerTick", 5);
        addInteger("moduleMovementEnergyPerTick", 5);
        addBoolean("AlwaysShowAdvancedInGameTooltip", false);
        addBoolean("YouTubeMode", false);
        addStringArray("ActivatedModulesWithEnergyPerTick", new String[]{"damageBoost@150", "heal@1500", "regeneration@150", "waterBreathing@200", "healthBoost@500", "absorption@250", "saturation@1000",
                "moveSlowdown@null", "digSlowDown@null", "damageBoost@null", "harm@null", "confusion@null", "blindness@null", "hunger@null", "weakness@null", "poison@null", "wither@null", "unluck@null"});
        addInteger("DefaultModuleEffectEnergyTick", 100);

        saveProperties();
    }

    private void addBoolean(String key, boolean value){
        if(!booleanProperties.containsKey(key)){
            booleanProperties.put(key, value);
        }
    }

    private void addInteger(String key, int value){
        if(!integerProperties.containsKey(key)){
            integerProperties.put(key, value);
        }
    }

    private void addStringArray(String key, String[] value){
        if(!stringArrayProperties.containsKey(key)){
            stringArrayProperties.put(key, value);
        }
    }

    private boolean isEntryBoolean(String entry){
        return entry.equals("true") || entry.equals("false");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean isEntryInteger(String entry){
        try{
            Integer.parseInt(entry);
        } catch(NumberFormatException e){
            return false;
        }
        return true;
    }

    private boolean isEntryStringArray(String entry){
        return entry.startsWith("[") && entry.endsWith("]");
    }

    private boolean parseBool(String bool){
        return bool.equals("true");
    }

}
