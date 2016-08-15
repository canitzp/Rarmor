/*
 * This file ("MethodHand.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.misc;

import de.ellpeck.rarmor.api.internal.IMethodHandler;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.data.RarmorData;
import de.ellpeck.rarmor.mod.data.WorldData;
import de.ellpeck.rarmor.mod.module.main.ActiveModuleMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;

public class MethodHandler implements IMethodHandler{

    @Override
    public IRarmorData getDataForChestplate(EntityPlayer player){
        ItemStack stack = player.inventory.armorInventory[EntityEquipmentSlot.CHEST.getIndex()];
        if(stack != null){
            return this.getDataForStack(player.worldObj, stack);
        }
        return null;
    }

    private void checkAndSetRarmorId(ItemStack stack){
        if(!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound compound = stack.getTagCompound();
        if(!compound.hasUniqueId("RarmorId")){
            compound.setUniqueId("RarmorId", UUID.randomUUID());
        }
    }

    @Override
    public IRarmorData getDataForUuid(World world, UUID stackId){
        Map<UUID, IRarmorData> data = WorldData.getRarmorData(world);
        if(data != null){
            IRarmorData theData = data.get(stackId);
            if(theData == null){
                theData = new RarmorData(stackId);

                ActiveRarmorModule module = Helper.initiateModuleById(ActiveModuleMain.IDENTIFIER, theData);
                module.onInstalled(null);
                theData.getCurrentModules().add(module);

                data.put(stackId, theData);
            }

            return theData;
        }
        return null;
    }

    @Override
    public IRarmorData getDataForStack(World world, ItemStack stack){
        this.checkAndSetRarmorId(stack);
        UUID stackId = stack.getTagCompound().getUniqueId("RarmorId");
        return this.getDataForUuid(world, stackId);
    }

    @Override
    public boolean compareModules(ActiveRarmorModule module, Object o){
        return o instanceof ActiveRarmorModule && ((ActiveRarmorModule)o).getIdentifier().equals(module.getIdentifier());
    }
}
