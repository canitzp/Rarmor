/*
 * This file 'ItemModule.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public class ItemModule extends Item{

    public static Map<String, ItemModule> modules = new HashMap<>();

    public ItemModule(String name){
        setMaxStackSize(1);
        modules.put(name, this);
        Rarmor.registerItem(this, name);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
        if(stack != null && stack.getItem() != null && stack.getItem() instanceof IRarmorModule){
            String desc = ((IRarmorModule) stack.getItem()).getDescription(playerIn, stack, advanced);
            if(desc != null){
                tooltip.addAll(Minecraft.getMinecraft().fontRendererObj.listFormattedStringToWidth(desc, 350));
            } else {
                super.addInformation(stack, playerIn, tooltip, advanced);
            }
            IRarmorModule.ModuleType moduleType = ((IRarmorModule) stack.getItem()).getModuleType();
            switch (moduleType){
                case ACTIVE: tooltip.add(TextFormatting.GREEN + "Active" + TextFormatting.GRAY); break;
                case PASSIVE: tooltip.add(TextFormatting.RED + "Passive" + TextFormatting.GRAY); break;
                case NONE: default: break;
            }
        } else {
            super.addInformation(stack, playerIn, tooltip, advanced);
        }
    }
}
