/*
 * This file ("ItemRarmor.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.item;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.misc.CreativeTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;
import java.util.Locale;

public class ItemRarmor extends ItemArmor{

    private static final ArmorMaterial RARMOR_MATERIAL = EnumHelper.addArmorMaterial(RarmorAPI.MOD_ID.toUpperCase(Locale.ROOT)+"_MATERIAL", RarmorAPI.MOD_ID+":rarmorArmor", 0, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);

    public ItemRarmor(String name, EntityEquipmentSlot slot){
        super(RARMOR_MATERIAL, -1, slot);

        this.setRegistryName(RarmorAPI.MOD_ID, name);
        GameRegistry.register(this);

        this.setUnlocalizedName(RarmorAPI.MOD_ID+"."+name);
        this.setCreativeTab(CreativeTab.INSTANCE);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
        if(this.isChestplate()){
            if(entity instanceof EntityPlayer){
                IRarmorData data = RarmorAPI.methodHandler.getDataForStack(world, stack, !world.isRemote);
                if(data != null){
                    data.tick(world);
                    data.sendQueuedUpdate((EntityPlayer)entity);
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced){
        if(this.isChestplate()){
            IRarmorData data = RarmorAPI.methodHandler.getDataForStack(player.worldObj, stack, false);
            if(data != null){
                String s = "   ";
                tooltip.add(TextFormatting.GOLD+I18n.format(RarmorAPI.MOD_ID+".installedModules")+":");
                for(ActiveRarmorModule module : data.getCurrentModules()){
                    tooltip.add(TextFormatting.YELLOW+s+"-"+I18n.format("module."+module.getIdentifier()+".name"));
                }

                tooltip.add(TextFormatting.GOLD+I18n.format(RarmorAPI.MOD_ID+".stackId")+":");
                tooltip.add(TextFormatting.YELLOW+s+data.getBoundStackId());
            }
            else{
                String s = TextFormatting.RED+""+TextFormatting.ITALIC;
                tooltip.add(s+I18n.format(RarmorAPI.MOD_ID+".noDataYet"));
                tooltip.add(s+I18n.format(RarmorAPI.MOD_ID+".noDataExplain"));
            }
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.RARE;
    }

    private boolean isChestplate(){
        return this.armorType == EntityEquipmentSlot.CHEST;
    }
}
