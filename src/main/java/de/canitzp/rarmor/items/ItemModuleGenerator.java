package de.canitzp.rarmor.items;

import cofh.api.energy.IEnergyContainerItem;
import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.util.inventory.InventoryBase;
import de.canitzp.util.util.ItemStackUtil;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemModuleGenerator extends ItemModule implements IRarmorModule {

    public ItemModuleGenerator() {
        super("moduleGenerator");
        setMaxStackSize(1);
    }

    @Override
    public String getUniqueName() {
        return "Generator";
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory) {
        if (isGenerator(inventory, armorChestplate) || NBTUtil.getInteger(module, "GenBurnTime") > 0) {
            this.generate(armorChestplate, module);
        } else {
            NBTUtil.setInteger(module, "GenBurnTime", 0);
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory) {
        dropSlot(inventory, inventory.getStackInSlot(30), player, armorChestplate);
        NBTUtil.setInteger(module, "GenBurnTime", 0);
    }

    private boolean isGenerator(IInventory inv, ItemStack armor){
        if(armor.getItem() instanceof IEnergyContainerItem){
            int burnTime = TileEntityFurnace.getItemBurnTime(inv.getStackInSlot(ItemRFArmorBody.GENERATORSLOT));
            int energyToProduce = burnTime * NBTUtil.getInteger(armor, "rfPerTick");
            return burnTime > 0 && NBTUtil.getInteger(armor, "Energy") + (energyToProduce / 4) <= ((IEnergyContainerItem) armor.getItem()).getMaxEnergyStored(armor);
        }
        return false;
    }

    private void generate(ItemStack armor, ItemStack module){
        InventoryBase inventory = NBTUtil.readSlots(armor, ((ItemRFArmorBody)armor.getItem()).slotAmount);
        int burnTime = NBTUtil.getInteger(module, "GenBurnTime");
        if(burnTime == 0){
            NBTUtil.setInteger(module, "CurrentItemGenBurnTime", TileEntityFurnace.getItemBurnTime(inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT)));
            System.out.println(NBTUtil.getInteger(module, "CurrentItemGenBurnTime"));
            ItemStack burnItem = inventory.getStackInSlot(ItemRFArmorBody.GENERATORSLOT);
            inventory = ItemStackUtil.reduceStackSize(inventory, ItemRFArmorBody.GENERATORSLOT);
            if(burnItem.getItem().getContainerItem() != null) {
                inventory = ItemStackUtil.addStackToSlot(inventory, new ItemStack(burnItem.getItem().getContainerItem()), ItemRFArmorBody.GENERATORSLOT);
            }
            NBTUtil.saveSlots(armor, inventory);
        }
        if(burnTime < NBTUtil.getInteger(module, "CurrentItemGenBurnTime")){
            burnTime++;
            System.out.println(burnTime);
            ((IEnergyContainerItem)armor.getItem()).receiveEnergy(armor, NBTUtil.getInteger(armor, "rfPerTick"), false);
        }else {
            burnTime = 0;
            NBTUtil.setInteger(module, "CurrentItemGenBurnTime", 0);
        }
        NBTUtil.setInteger(module, "GenBurnTime", burnTime);
    }

    private void dropSlot(InventoryBase inventory, ItemStack stack, EntityPlayer player, ItemStack armor) {
        if(stack != null){
            if(!player.worldObj.isRemote){
                player.dropPlayerItemWithRandomChoice(stack, false);
            }
            inventory.setInventorySlotContents(30, null);
            NBTUtil.saveSlots(armor, inventory);
        }
    }
}
