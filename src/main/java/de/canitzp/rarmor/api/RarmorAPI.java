package de.canitzp.rarmor.api;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class RarmorAPI{

    public static final String OWNER = "rarmor";
    public static final String PROVIDES = "rarmorAPI";
    public static final String VERSION = "2.0.0";

    public static List<IRarmorTab> registeredTabs = new ArrayList<>();

    public static IRarmorTab getTab(int id){
        if(registeredTabs.size() >= id){
            return registeredTabs.get(id);
        }
        return registeredTabs.get(0);
    }

}
