package de.canitzp.rarmor.items.rfarmor.modules;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.util.JavaUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author canitzp
 */
public class ItemModuleFastFurnace extends ItemModule implements IRarmorModule{

    public ItemModuleFastFurnace(){
        super("moduleFastFurnace");
    }

    @Override
    public String getUniqueName(){
        return "FastFurnace";
    }

    @Override
    public List<String> getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return JavaUtil.newList("The FastFurnace Module increases your",
                "Furnace speed by" + TextFormatting.BLUE + " 2" + TextFormatting.GRAY + ",",
                "but it also needs " + TextFormatting.BLUE + "300%" + TextFormatting.GRAY + " of the normal energy usage.");
    }

    @Override
    public ModuleType getModuleType(){
        return ModuleType.PASSIVE;
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        NBTUtil.setIntegerIfNot(armorChestplate, "rfPerTick", 60);
        NBTUtil.setIntegerIfNot(armorChestplate, "BurnTimeMultiplier", 2);
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, SlotUpdate slot){
        NBTUtil.setIntegerIfNot(armorChestplate, "rfPerTick", ItemRFArmorBody.rfPerTick);
        NBTUtil.setIntegerIfNot(armorChestplate, "BurnTimeMultiplier", 1);
    }

}
