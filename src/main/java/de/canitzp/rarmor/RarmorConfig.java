package de.canitzp.rarmor;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/**
 * @author canitzp
 */
public class RarmorConfig {

    private static Configuration config;

    public static void initializeConfiguration(FMLPreInitializationEvent event){
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        init(config);
        config.save();
    }

    private static void init(Configuration config){
        for(ConfigValues value : ConfigValues.values()){
            if(value.defaultValue instanceof Boolean){
                value.currentValue = config.get(value.category.name, value.name, (Boolean) value.defaultValue, value.desc).getBoolean();
            } else if(value.defaultValue instanceof Integer){
                value.currentValue = config.get(value.category.name, value.name, (Integer) value.defaultValue, value.desc).getInt();
            } else if(value.defaultValue instanceof int[]){
                value.currentValue = config.get(value.category.name, value.name, (int[]) value.defaultValue, value.desc).getIntList();
            }
        }
    }

    public static void reloadConfig(){
        config.load();
        init(config);
        if(config.hasChanged()){
            config.save();
        }
    }

    enum ConfigCategories{
        RARMOR("Rarmor", "All Values for the Armor"),
        RARMOR_TABS("Rarmor Tabs", "All Values for the Rarmor Tabs"),
        OTHER("Other", "");

        public String name, comment;
        ConfigCategories(String name, String comment) {
            this.name = name;
            this.comment = comment;
        }
    }

    enum ConfigValues{
        RARMOR_MAX_ENERGY("Rarmor max energy", ConfigCategories.RARMOR, 250000, "The max amount of electrical energy a armor part can hold"),
        RARMOR_MAX_TRANSFER("Rarmor max transfer", ConfigCategories.RARMOR, 25000, "The max amount that can be transferred from or to the rarmor."),

        GENERATOR_MAX_RECEIVE("Generator max generate", ConfigCategories.RARMOR_TABS, 40, "The max amount of generation per tick"),

        TOOLTIPS_ALWAYS_ACTIVE("InWorld-Tooltips always active", ConfigCategories.OTHER, false, "Are the Rarmor InWorld-Tooltips active without wearing the Armor");

        public String name, desc;
        public ConfigCategories category;
        public Object defaultValue;
        public Object currentValue;
        ConfigValues(String name, ConfigCategories category, Object defaultValue, String desc) {
            this.name = name;
            this.desc = desc;
            this.category = category;
            this.defaultValue = defaultValue;
        }
        public boolean getBoolean(){
            return currentValue instanceof Boolean && (Boolean) currentValue;
        }
        public int getInteger(){
            return currentValue instanceof Integer ? (int) currentValue : (int) defaultValue;
        }
        public int[] getIntList(){
            return currentValue instanceof int[] ? (int[]) currentValue : (int[]) defaultValue;
        }
    }

}
