package de.canitzp.rarmor.api;

import de.canitzp.rarmor.api.tooltip.IInWorldTooltip;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author canitzp
 */
public class RarmorAPI{

    public static List<Class<? extends IRarmorTab>> registeredTabs = new ArrayList<>();
    public static Map<Integer, String> registerColor = new HashMap<>();
    public static Map<UUID, List<IRarmorTab>> tabListServer = new HashMap<>();
    public static Map<UUID, List<IRarmorTab>> tabListClient = new HashMap<>();
    private static List<IInWorldTooltip> registeredTooltips = new ArrayList<>();

    public static void registerInWorldTooltip(IInWorldTooltip tooltip){
        if(!registeredTooltips.contains(tooltip)){
            registeredTooltips.add(tooltip);
        }
    }

    public static List<IInWorldTooltip> getInWorldTooltips(){
        return registeredTooltips;
    }

    public static void registerRarmorTab(Class<? extends IRarmorTab> classToRegister){
        if(!registeredTabs.contains(classToRegister)){
            registeredTabs.add(classToRegister);
        }
    }

    public static Map<UUID, List<IRarmorTab>> getSidedTabs(World world){
        return world.isRemote ? tabListClient : tabListServer;
    }

    public static boolean hasRarmorTabs(World world, ItemStack stack){
        return getTabsFromStack(world, stack) != null;
    }

    public static List<IRarmorTab> getTabsFromStack(World world, ItemStack stack){
        NBTTagCompound nbt = stack.getTagCompound();
        if(nbt != null){
            UUID uuid = nbt.getUniqueId("RarmorUUID");
            return getSidedTabs(world).containsKey(uuid) ? getSidedTabs(world).get(uuid) : null;
        }
        return null;
    }

    public static void setNewTabsToStack(EntityPlayer player, ItemStack stack){
        UUID uuid = UUID.randomUUID();
        if(!RarmorAPI.getSidedTabs(player.getEntityWorld()).containsKey(uuid)){
            NBTTagCompound nbt = stack.getTagCompound();
            if(nbt == null){
                stack.setTagCompound(new NBTTagCompound());
            }
            stack.getTagCompound().setUniqueId("RarmorUUID", uuid);
            RarmorAPI.getSidedTabs(player.getEntityWorld()).put(uuid, createNewTabs(player, stack));
        } else {
            setNewTabsToStack(player, stack);
        }
    }

    public static void registerColor(int hexValue, String name){
        if(!registerColor.keySet().contains(hexValue)){
            registerColor.put(hexValue, name);
        }
    }

    private static List<IRarmorTab> createNewTabs(EntityPlayer player, ItemStack stack){
        List<IRarmorTab> tickables = new ArrayList<>();
        for(Class<? extends IRarmorTab> tab : registeredTabs){
            try{
                try{
                    tickables.add(tab.getConstructor(EntityPlayer.class, ItemStack.class).newInstance(player, stack));
                } catch (NoSuchMethodException e){
                    tickables.add(tab.newInstance());
                }
            } catch(InstantiationException | IllegalAccessException | InvocationTargetException e){
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
