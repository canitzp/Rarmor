/*
 * This file 'ItemRFArmorBody.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.RarmorProperties;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.util.EnergyUtil;
import de.canitzp.rarmor.util.ItemStackUtil;
import de.canitzp.rarmor.util.NBTUtil;
import de.canitzp.rarmor.util.PlayerUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author canitzp
 */
@SuppressWarnings("unchecked")
public class ItemRFArmorBody extends ItemRFArmor{

    public static final int FURNACEINPUT = 27, FURNACEOUTPUT = 28, MODULESLOT = 29, GENERATORSLOT = 30;
    public static int rfPerTick = 20, slotAmount = 34;

    public ItemRFArmorBody(){
        super(ItemRFArmor.RFARMOR, EntityEquipmentSlot.CHEST, 250000, 1500, "rfArmorBody");
        rfPerTick = RarmorProperties.getInteger("maxRarmorTransferPerTick");
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player){
        EnergyUtil.setEnergy(stack, 0);
        NBTUtil.setBoolean(stack, "isFirstOpened", false);
        NBTUtil.setInteger(stack, "rfPerTick", rfPerTick);
        NBTUtil.setInteger(stack, "color", tabColor.colorValue);
        NBTUtil.setString(stack, "colorName", tabColor.getName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list){
        ItemStack stack = new ItemStack(this);
        NBTUtil.setInteger(stack, "color", tabColor.colorValue);
        NBTUtil.setString(stack, "colorName", tabColor.getName());
        EnergyUtil.setEnergy(stack, this.getMaxEnergyStored(stack));
        NBTUtil.setBoolean(stack, "isFirstOpened", false);
        NBTUtil.setInteger(stack, "rfPerTick", rfPerTick);
        list.add(stack);
        ItemStack stack1 = stack.copy();
        EnergyUtil.setEnergy(stack1, 0);
        list.add(stack1);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        double energyDif = this.maxEnergy - NBTUtil.getInteger(stack, "Energy");
        double maxAmount = this.maxEnergy;
        return energyDif / maxAmount;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack armor){
        if(!world.isRemote){
            this.handleModules(world, player, armor);
            if(NBTUtil.getBoolean(armor, "isFirstOpened")){
                if(NBTUtil.getInteger(armor, "rfPerTick") == 0) NBTUtil.setInteger(armor, "rfPerTick", rfPerTick);
                if(NBTUtil.getInteger(armor, "BurnTimeMultiplier") == 0)
                    NBTUtil.setInteger(armor, "BurnTimeMultiplier", 1);
                ItemStack foot = PlayerUtil.getArmor(player, EntityEquipmentSlot.FEET);
                ItemStack leggins = PlayerUtil.getArmor(player, EntityEquipmentSlot.LEGS);
                ItemStack head = PlayerUtil.getArmor(player, EntityEquipmentSlot.HEAD);
                if(RarmorUtil.isPlayerWearingRarmor(player)){
                    if(isBurnable(armor)){
                        burn(armor);
                    } else NBTUtil.setInteger(armor, "BurnTime", 0);
                    if(NBTUtil.getInteger(armor, "Energy") - NBTUtil.getInteger(foot, "Energy") >= 4 || NBTUtil.getInteger(armor, "Energy") - NBTUtil.getInteger(foot, "Energy") <= 4){
                        EnergyUtil.balanceEnergy(new ItemStack[]{foot, armor, leggins, head});
                    }
                }
            }
        }
    }

    private void handleModules(World world, EntityPlayer player, ItemStack armor){
        InventoryBase inventory = RarmorUtil.readRarmor(armor);
        ItemStack module = inventory.getStackInSlot(MODULESLOT);
        if(module != null && module.getItem() instanceof IRarmorModule){
            if(NBTUtil.getBoolean(module, "FirstOpenedModule")){
                ((IRarmorModule) module.getItem()).initModule(world, player, armor, module, inventory);
                NBTUtil.setBoolean(module, "FirstOpenedModule", false);
            }
            IRarmorModule mod = (IRarmorModule) module.getItem();
            mod.onModuleTickInArmor(world, player, armor, module, inventory);
        }
    }

    private boolean isBurnable(ItemStack armor){
        InventoryBase inventory = RarmorUtil.readRarmor(armor);
        if(inventory != null){
            ItemStack input = inventory.getStackInSlot(FURNACEINPUT);
            if(input != null && FurnaceRecipes.instance().getSmeltingResult(input) != null){
                ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
                if(this.extractEnergy(armor, NBTUtil.getInteger(armor, "rfPerTick") * 200, true) > 0){
                    ItemStack output = inventory.getStackInSlot(FURNACEOUTPUT);
                    if(output != null && result.isItemEqual(output)){
                        if(output.stackSize + result.stackSize <= inventory.getInventoryStackLimit()){
                            return true;
                        }
                    } else if(output == null){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void burn(ItemStack body){
        int burnTime = NBTUtil.getInteger(body, "BurnTime");
        if(burnTime < 200){
            burnTime += NBTUtil.getInteger(body, "BurnTimeMultiplier");
            this.extractEnergy(body, NBTUtil.getInteger(body, "rfPerTick"), false);
        } else {
            smeltItem(body);
            burnTime = 0;
        }
        NBTUtil.setInteger(body, "BurnTime", burnTime);
    }

    public void smeltItem(ItemStack body){
        InventoryBase inventory = RarmorUtil.readRarmor(body);
        ItemStack input = inventory.getStackInSlot(FURNACEINPUT);
        ItemStack output = inventory.getStackInSlot(FURNACEOUTPUT);
        if(input != null){
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
            if(result != null){
                if(output == null || output.isItemEqual(result.copy())){
                    inventory = ItemStackUtil.reduceStackSize(inventory, FURNACEINPUT);
                    inventory = ItemStackUtil.addStackToSlot(inventory, result.copy(), FURNACEOUTPUT);
                    RarmorUtil.saveRarmor(body, inventory);
                }
            }
        }
    }
}