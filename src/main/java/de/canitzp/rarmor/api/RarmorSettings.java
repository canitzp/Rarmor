package de.canitzp.rarmor.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author canitzp
 */
public class RarmorSettings {

    private static NBTTagCompound getSettingsNBT(ItemStack stack){
        NBTTagCompound nbt = stack.getTagCompound();
        if(nbt != null){
            if(nbt.hasKey("RarmorSettings")){
                return nbt.getCompoundTag("RarmorSettings");
            } else {
                nbt.setTag("RarmorSettings", new NBTTagCompound());
                return getSettingsNBT(stack);
            }
        } else {
            stack.setTagCompound(new NBTTagCompound());
            return getSettingsNBT(stack);
        }
    }

    public static void setSetting(ItemStack stack, Settings setting, Object value){
        NBTTagCompound nbt = getSettingsNBT(stack);
        switch (setting.type){
            case BOOLEAN:{
                nbt.setBoolean(setting.name, (Boolean) value);
                break;
            }
            case INTEGER:{
                nbt.setInteger(setting.name, (Integer) value);
                break;
            }
        }
    }

    public static Object getSetting(ItemStack stack, Settings setting){
        NBTTagCompound nbt = getSettingsNBT(stack);
        switch (setting.type){
            case BOOLEAN:{
                return nbt.getBoolean(setting.name);
            }
            case INTEGER:{
                return nbt.getInteger(setting.name);
            }
        }
        return null;
    }

    public static boolean getSettingBoolean(ItemStack stack, Settings setting){
        Object o = getSetting(stack, setting);
        return o != null && o instanceof Boolean && (Boolean) o;
    }

    public enum Settings{
        INVERTED_OPENING("InvertedGuiOpening", SettingType.BOOLEAN, false),
        INWORLDTOOLTIPS("InWorldSettings", SettingType.BOOLEAN, false);

        public String name;
        public SettingType type;
        public Object defaultValue;
        Settings(String name, SettingType type, Object defaultValue) {
            this.name = name;
            this.type = type;
            this.defaultValue = defaultValue;
        }
        enum SettingType{
            BOOLEAN,
            INTEGER
        }
    }

}
