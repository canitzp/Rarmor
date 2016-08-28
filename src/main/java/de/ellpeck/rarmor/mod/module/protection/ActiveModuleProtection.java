/*
 * This file ("ActiveModuleProtection.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.protection;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.inventory.RarmorModuleContainer;
import de.ellpeck.rarmor.api.inventory.RarmorModuleGui;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

public class ActiveModuleProtection extends ActiveRarmorModule{

    public static final String[] TYPES = new String[]{"Leather", "Chain", "Iron", "Gold", "Diamond"};
    public static final String[] IDENTIFIERS = new String[TYPES.length];
    static{
        for(int i = 0; i < IDENTIFIERS.length; i++){
            IDENTIFIERS[i] = RarmorAPI.MOD_ID+"Protection"+TYPES[i];
        }
    }

    private static final double[] AMOUNTS = new double[]{2, 4, 5, 3, 6};

    private final int armorType;
    public final AttributeModifier additionalArmor;

    public ActiveModuleProtection(IRarmorData data, int armorType){
        super(data);
        this.armorType = armorType;
        this.additionalArmor = new AttributeModifier(UUID.fromString("21a856c5-e511-409b-a762-60299e1ebe7d"), RarmorAPI.MOD_ID+"ArmorModule", AMOUNTS[this.armorType], 0);
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIERS[this.armorType];
    }

    @Override
    public void tick(World world, Entity entity){

    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean sync){

    }

    @Override
    public void writeToNBT(NBTTagCompound compound, boolean sync){

    }

    @Override
    public RarmorModuleContainer createContainer(EntityPlayer player, Container container){
        return null;
    }

    @Override
    public RarmorModuleGui createGui(GuiContainer gui){
        return null;
    }

    @Override
    public void onInstalled(Entity entity){
        if(entity instanceof EntityLivingBase){
            IAttributeInstance attrib = ((EntityLivingBase)entity).getEntityAttribute(SharedMonsterAttributes.ARMOR);
            if(attrib != null && !attrib.hasModifier(this.additionalArmor)){
                attrib.applyModifier(this.additionalArmor);
            }
        }
    }

    @Override
    public void onUninstalled(Entity entity){
        if(entity instanceof EntityLivingBase){
            IAttributeInstance attrib = ((EntityLivingBase)entity).getEntityAttribute(SharedMonsterAttributes.ARMOR);
            if(attrib != null && attrib.hasModifier(this.additionalArmor)){
                attrib.removeModifier(this.additionalArmor);
            }
        }
    }

    @Override
    public boolean hasTab(EntityPlayer player){
        return false;
    }

    @Override
    public ItemStack getDisplayIcon(){
        return null;
    }

    @Override
    public void renderAdditionalOverlay(Minecraft mc, EntityPlayer player, IRarmorData data, ScaledResolution resolution, int renderX, int renderY, float partialTicks){

    }

    public static class Leather extends ActiveModuleProtection{

        public Leather(IRarmorData data){
            super(data, 0);
        }
    }

    public static class Chain extends ActiveModuleProtection{

        public Chain(IRarmorData data){
            super(data, 1);
        }
    }

    public static class Iron extends ActiveModuleProtection{

        public Iron(IRarmorData data){
            super(data, 2);
        }
    }

    public static class Gold extends ActiveModuleProtection{

        public Gold(IRarmorData data){
            super(data, 3);
        }
    }

    public static class Diamond extends ActiveModuleProtection{

        public Diamond(IRarmorData data){
            super(data, 4);
        }
    }
}
