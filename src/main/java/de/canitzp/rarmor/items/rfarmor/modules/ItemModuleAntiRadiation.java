package de.canitzp.rarmor.items.rfarmor.modules;

import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IModuleIntegration;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

/**
 * @author canitzp
 */
public class ItemModuleAntiRadiation extends ItemModule implements IModuleIntegration{

    private boolean isModuleLoaded;

    public ItemModuleAntiRadiation(){
        super("moduleAntiRadiation");
        this.isModuleLoaded = Loader.isModLoaded("deepresonance");
    }

    @Override
    public boolean isModuleLoaded(){
        return this.isModuleLoaded;
    }

    @Override
    public String getUniqueName(){
        return "AntiRadiation";
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, SlotUpdate slot){
        armorChestplate.getTagCompound().removeTag("AntiRadiationArmor");
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        NBTUtil.setBoolean(armorChestplate, "AntiRadiationArmor", true);
    }
}
