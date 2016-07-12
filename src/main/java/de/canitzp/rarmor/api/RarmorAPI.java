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

    public static List<Class<? extends IRarmorTab>> registeredTabs = new ArrayList<>();
    public static Map<Integer, String> registerColor = new HashMap<>();
    public static Map<ItemStack, List<IRarmorTab>> tickMap = new HashMap<>();

    public static void registerRarmorTab(Class<? extends IRarmorTab> classToRegister){
        if(!registeredTabs.contains(classToRegister)){
            registeredTabs.add(classToRegister);
        }
    }

    public static List<IRarmorTab> getNewTabs(ItemStack stack){
        return tickMap.get(stack);
    }

    public static IRarmorTab getPossibleActiveTab(EntityPlayer player, ItemStack rarmor, NBTTagCompound rarmorNBT){
        List<IRarmorTab> tabs = getNewTabs(rarmor);
        if(RarmorAPI.tickMap.get(rarmor) == null){
            RarmorAPI.tickMap.put(rarmor, createNewTabs());
            tabs = getNewTabs(rarmor);
        }
        if(!tabs.isEmpty()){
            IRarmorTab tab = tabs.get(rarmorNBT.getInteger("RarmorTabID"));
            return tab.canBeVisible(rarmor, player) ? tab : tabs.get(0);
        }
        return getNewTabs(rarmor).get(0);
    }

    public static void registerColor(int hexValue, String name){
        if(!registerColor.keySet().contains(hexValue)){
            registerColor.put(hexValue, name);
        }
    }

    public static List<IRarmorTab> createNewTabs(){
        List<IRarmorTab> tickables = new ArrayList<>();
        for(Class<? extends IRarmorTab> tab : registeredTabs){
            try{
                tickables.add(tab.newInstance());
            } catch(InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
        return tickables;
    }

    public static int receiveEnergy(ItemStack container, int receive, boolean simulate){
        NBTTagCompound nbt = container.getTagCompound();
        if(nbt != null){
            int currentEnergy = nbt.getInteger("Energy");
            int energyReceived = Math.min(RarmorValues.rarmorMaxEnergy - currentEnergy, receive);
            if(!simulate){
                nbt.setInteger("Energy", currentEnergy + energyReceived);
            }
            return energyReceived;
        }
        return 0;
    }

    public static int extractEnergy(ItemStack container, int extract, boolean simulate){
        NBTTagCompound nbt = container.getTagCompound();
        if(nbt != null){
            int currentEnergy = nbt.getInteger("Energy");
            int energyExtracted = Math.min(currentEnergy, extract);
            if(!simulate){
                nbt.setInteger("Energy", currentEnergy + energyExtracted);
            }
            return energyExtracted;
        }
        return 0;
    }
}
