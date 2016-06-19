package de.canitzp.rarmor;

import de.canitzp.rarmor.armor.ItemGenericRarmor;
import de.canitzp.rarmor.armor.ItemRarmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author canitzp
 */
public class Registry{

    public static Item rarmorChestplate, rarmorHelmet, rarmorLeggins, rarmorBoots;

    public static void initItems(FMLPreInitializationEvent event){
        rarmorHelmet = new ItemGenericRarmor(EntityEquipmentSlot.HEAD);
        rarmorChestplate = new ItemRarmor(EntityEquipmentSlot.CHEST);
        rarmorLeggins = new ItemGenericRarmor(EntityEquipmentSlot.LEGS);
        rarmorBoots = new ItemGenericRarmor(EntityEquipmentSlot.FEET);
    }



    public static void registerItem(Item item, String name){
        item.setUnlocalizedName(Rarmor.MODID + ":" + name);
        item.setRegistryName(Rarmor.MODID, name);
        Rarmor.proxy.registerTexture(new ItemStack(item), name);
        GameRegistry.register(item);
    }

}
