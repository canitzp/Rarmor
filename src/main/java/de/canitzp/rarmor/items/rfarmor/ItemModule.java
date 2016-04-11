package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public class ItemModule extends Item {

    public static Map<String, ItemModule> modules = new HashMap<>();

    public ItemModule(String name) {
        setMaxStackSize(1);
        modules.put(name, this);
        Rarmor.registerItem(this, name);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if(stack != null && stack.getItem() != null && stack.getItem() instanceof IRarmorModule){
            List<String> desc = ((IRarmorModule) stack.getItem()).getDescription(playerIn, stack, advanced);
            if(desc != null){
                tooltip.addAll(desc);
            } else {
                super.addInformation(stack, playerIn, tooltip, advanced);
            }
        } else {
            super.addInformation(stack, playerIn, tooltip, advanced);
        }
    }
}
