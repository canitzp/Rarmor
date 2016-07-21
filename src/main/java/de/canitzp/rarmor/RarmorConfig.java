package de.canitzp.rarmor;

import de.canitzp.rarmor.api.RarmorValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            } else if(value.defaultValue instanceof Double){
                value.currentValue = config.get(value.category.name, value.name, (Double) value.defaultValue, value.desc).getDouble();
            }
        }
        for(ConfigCategories categorie : ConfigCategories.values()){
            config.getCategory(categorie.name).setComment(categorie.comment);
        }
    }

    public static void reloadConfig(){
        init(config);
        if(config.hasChanged()){
            config.save();
        }
        Rarmor.redefineValues();
    }

    enum ConfigCategories{
        RARMOR("Rarmor", "All Values for the Armor"),
        RARMOR_TABS("Rarmor_Tabs", "All Values for the Rarmor Tabs"),
        INWORLDTOOLTIPS("Tooltips", "All Values for the InWorld-Tooltips");

        public String name, comment;
        ConfigCategories(String name, String comment) {
            this.name = name;
            this.comment = comment;
        }
    }

    enum ConfigValues{
        RARMOR_MAX_ENERGY("Rarmor max energy", ConfigCategories.RARMOR, 250000, "The max amount of electrical energy a armor part can hold (Default: 250000)"),
        RARMOR_MAX_TRANSFER("Rarmor max transfer", ConfigCategories.RARMOR, 25000, "The max amount that can be transferred from or to the rarmor (Default: 25000)"),

        GENERATOR_MAX_RECEIVE("Generator max generate", ConfigCategories.RARMOR_TABS, 40, "The max amount of generation per tick (Default: 40)"),

        TOOLTIPS_ALWAYS_ACTIVE("InWorld-Tooltips always active", ConfigCategories.INWORLDTOOLTIPS, false, "Are the Rarmor InWorld-Tooltips active without wearing the Armor (Default: false)"),
        TOOLTIPS_DEFAULT_POSITION("InWorld-Tooltips default offset", ConfigCategories.INWORLDTOOLTIPS, new int[]{0, 5}, "The default offset of the tooltips. (Default: x=0, y=5)"),
        TOOLTIPS_DEFAULT_TEXTCOLOR("InWorld-Tooltips default text color", ConfigCategories.INWORLDTOOLTIPS, 0xFFFFFF, "The default text color as decimal or hexadecimal code (Default: (HEX)0xFFFFFF or (DEC)16777215)"),
        TOOLTIPS_DEFAULT_SCALE("InWorld-Tooltips default scaling size", ConfigCategories.INWORLDTOOLTIPS, 1.0, "The default scaling of the tooltip text (Default: 1.0)");

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
        public double getDouble(){
            return currentValue instanceof Double ? (double) currentValue : (double) defaultValue;
        }
        public float getFloat(){
            return (float) getDouble();
        }
    }

    public static class GuiConfigurationFactory implements IModGuiFactory{
        @Override
        public void initialize(Minecraft minecraftInstance) {}
        @Override
        public Class<? extends GuiScreen> mainConfigGuiClass() {return GuiConfiguration.class;}
        @Override
        public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {return null;}
        @Override
        public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {return null;}
    }

    public static class GuiConfiguration extends GuiConfig {
        public GuiConfiguration(GuiScreen parentScreen) {
            super(parentScreen, getConfigElements(), RarmorValues.MODID, false, false, getAbridgedConfigPath(config.toString()));
        }
        private static List<IConfigElement> getConfigElements(){
            List<IConfigElement> list = new ArrayList<>();
            for(ConfigCategories categorie : ConfigCategories.values()){
                list.add(new ConfigElement(config.getCategory(categorie.name)));
            }
            return list;
        }

        @Override
        public void onGuiClosed() {
            reloadConfig();
            super.onGuiClosed();
        }
    }

}
