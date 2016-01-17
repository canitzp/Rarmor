package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.util.inventory.InventoryBase;
import de.canitzp.util.util.EnergyUtil;
import de.canitzp.util.util.ItemStackUtil;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class ItemRFArmorBody extends ItemRFArmor {

    public int rfPerTick = 20, slotAmount = 31;
    private final int FURNACEINPUT = 27, FURNACEOUTPUT = 28, MODULESLOT = 29, GENERATORSLOT = 30;

    public ItemRFArmorBody() {
        super(ItemRFArmor.RFARMOR, ArmorType.BODY, 250000, 1500, "rfArmorBody");
        setHasSubtypes(true);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player){
        this.setEnergy(stack, 0);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        double energyDif = this.maxEnergy- NBTUtil.getInteger(stack, "Energy");
        double maxAmount = this.maxEnergy;
        return energyDif/maxAmount;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack armor) {
        ItemStack foot = player.getCurrentArmor(0);
        ItemStack leggins = player.getCurrentArmor(1);
        ItemStack head = player.getCurrentArmor(3);
        if (this.isArmor(foot, leggins, armor, head)) {
            if (isBurnable(armor)) {
                burn(armor);
            } else NBTUtil.setInteger(armor, "BurnTime", 0);
            this.handleModules(armor);
            EnergyUtil.balanceEnergy(new ItemStack[]{foot, leggins, armor, head});
        }

    }

    private void handleModules(ItemStack armor){
        IInventory inventory = NBTUtil.readSlots(armor, this.slotAmount);
        if (isGenerator(inventory, armor) || NBTUtil.getInteger(armor, "GenBurnTime") > 0) {
            this.generate(armor);
        } else {
            NBTUtil.setInteger(armor, "GenBurnTime", 0);
        }
    }

    private boolean isArmor(ItemStack foot, ItemStack leggins, ItemStack armor, ItemStack head) {
        if(head != null && foot != null && leggins != null && armor != null) {
            if (head.getItem() instanceof ItemRFArmorGeneric && foot.getItem() instanceof ItemRFArmorGeneric && leggins.getItem() instanceof ItemRFArmorGeneric && armor.getItem() instanceof ItemRFArmorBody) {
                return true;
            }
        }
        return false;
    }

    private boolean isBurnable(ItemStack armor){
        InventoryBase inventory = NBTUtil.readSlots(armor, this.slotAmount);
        if(inventory != null){
            ItemStack input = inventory.getStackInSlot(FURNACEINPUT);
            if(input != null && FurnaceRecipes.instance().getSmeltingResult(input) != null){
                ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
                if(this.extractEnergy(armor, this.rfPerTick * 200, true) >= 0){
                    ItemStack output = inventory.getStackInSlot(FURNACEOUTPUT);
                    if(output != null && result.isItemEqual(output)){
                        if(output.stackSize+result.stackSize<=inventory.getInventoryStackLimit()){
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

    private boolean isGenerator(IInventory inv, ItemStack armor){
        if(inv != null && NBTUtil.getBoolean(armor, "ModuleGenerator")){
            int burnTime = TileEntityFurnace.getItemBurnTime(inv.getStackInSlot(GENERATORSLOT));
            int energyToProduce = burnTime * this.rfPerTick;
            return burnTime > 0 && this.getEnergyStored(armor) + (energyToProduce / 4) <= this.maxEnergy;
        }
        return false;
    }

    public void burn(ItemStack body){
        int burnTime = NBTUtil.getInteger(body, "BurnTime");
        if(burnTime < 200){
            burnTime++;
            this.extractEnergy(body, rfPerTick, false);
        } else {
            smeltItem(body);
            burnTime = 0;
        }
        NBTUtil.setInteger(body, "BurnTime", burnTime);
    }

    public void smeltItem(ItemStack body){
        InventoryBase inventory = NBTUtil.readSlots(body, this.slotAmount);
        ItemStack input = inventory.getStackInSlot(FURNACEINPUT);
        ItemStack output = inventory.getStackInSlot(FURNACEOUTPUT);
        if(input != null) {
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
            if (result != null) {
                if(output == null || output.isItemEqual(result.copy())){
                    inventory = ItemStackUtil.reduceStackSize(inventory, FURNACEINPUT);
                    inventory = ItemStackUtil.addStackToSlot(inventory, result.copy(), FURNACEOUTPUT);
                    NBTUtil.saveSlots(body, inventory);
                }
            }
        }
    }

    private void generate(ItemStack stack){
        InventoryBase inventory = NBTUtil.readSlots(stack, this.slotAmount);
        int burnTime = NBTUtil.getInteger(stack, "GenBurnTime");
        if(burnTime == 0){
            NBTUtil.setInteger(stack, "CurrentItemGenBurnTime", TileEntityFurnace.getItemBurnTime(inventory.getStackInSlot(GENERATORSLOT)));
            ItemStack burnItem = inventory.getStackInSlot(GENERATORSLOT);
            inventory = ItemStackUtil.reduceStackSize(inventory, GENERATORSLOT);
            if(burnItem.getItem().getContainerItem() != null){
                inventory = ItemStackUtil.addStackToSlot(inventory, new ItemStack(burnItem.getItem().getContainerItem()), GENERATORSLOT);
            }
            NBTUtil.saveSlots(stack, inventory);
        }
        if(burnTime < NBTUtil.getInteger(stack, "CurrentItemGenBurnTime")){
            burnTime++;
            this.receiveEnergy(stack, rfPerTick, false);
        }else {
            burnTime = 0;
            NBTUtil.setInteger(stack, "CurrentItemGenBurnTime", 0);
        }
        NBTUtil.setInteger(stack, "GenBurnTime", burnTime);
    }

}
