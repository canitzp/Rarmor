package de.canitzp.rarmor.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public class RarmorAPI{

    public static final String OWNER = "rarmor";
    public static final String PROVIDES = "rarmorAPI";
    public static final String VERSION = "2.0.0";

    public static List<Class<? extends IRarmorTab>> registeredTabs = new ArrayList<>();
    public static List<Class<? extends ITabTickable>> registeredTickTabs = new ArrayList<>();
    public static Map<Integer, String> registerColor = new HashMap<>();

    public static void registerRarmorTab(Class<? extends IRarmorTab> classToRegister){
        if(!registeredTabs.contains(classToRegister)){
            registeredTabs.add(classToRegister);
        }
    }

    public static void registerITabTickable(Class<? extends ITabTickable> classToRegister){
        if(!registeredTickTabs.contains(classToRegister)){
            registeredTickTabs.add(classToRegister);
        }
        registerRarmorTab(classToRegister);
    }

    public static Class<? extends IRarmorTab> getTab(int id){
        if(registeredTabs.size() >= id){
            return registeredTabs.get(id);
        }
        return registeredTabs.get(0);
    }

    public static List<IRarmorTab> getNewTabs(){
        List<IRarmorTab> tabs = new ArrayList<>();
        for(Class<? extends IRarmorTab> clazz : registeredTabs){
            try{
                tabs.add(clazz.newInstance());
            } catch(InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
        return tabs;
    }

    public static IRarmorTab getPossibleActiveTab(EntityPlayer player, ItemStack rarmor, NBTTagCompound rarmorNBT){
        try{
            IRarmorTab tab = getTab(rarmorNBT.getInteger("RarmorTabID")).newInstance();
            return tab.canBeVisible(rarmor, player) ? tab : RarmorAPI.getTab(0).newInstance();
        } catch(InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void registerColor(int hexValue, String name){
        if(!registerColor.keySet().contains(hexValue)){
            registerColor.put(hexValue, name);
        }
    }

    public static List<ITabTickable> getNewTickTabs(){
        List<ITabTickable> tickables = new ArrayList<>();
        for(Class<? extends ITabTickable> tab : registeredTickTabs){
            try{
                tickables.add(tab.newInstance());
            } catch(InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
        return tickables;
    }

}
