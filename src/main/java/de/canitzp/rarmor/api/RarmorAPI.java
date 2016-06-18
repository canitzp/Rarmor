package de.canitzp.rarmor.api;

import com.google.common.collect.Lists;
import de.canitzp.rarmor.armor.RarmorInventoryTab;
import de.canitzp.rarmor.armor.RarmorOverviewTab;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class RarmorAPI{

    public static final String OWNER = "rarmor";
    public static final String PROVIDES = "rarmorAPI";
    public static final String VERSION = "2.0.0";

    public static List<Class<? extends IRarmorTab>> registeredTabs = Lists.newArrayList(RarmorOverviewTab.class, RarmorInventoryTab.class);

    public static void registerRarmorTab(Class<? extends IRarmorTab> classToRegister){
        if(!registeredTabs.contains(classToRegister)){
            registeredTabs.add(classToRegister);
        }
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

}
