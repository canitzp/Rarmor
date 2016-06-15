package de.canitzp.rarmor;

import de.canitzp.rarmor.armor.ItemRarmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author canitzp
 */
public class Registry{

    public static Item rarmorChestplate;

    public static void initItems(FMLPreInitializationEvent event){
        rarmorChestplate = new ItemRarmor(EntityEquipmentSlot.CHEST);
    }



    public static void registerItem(Item item, String name){
        item.setUnlocalizedName(Rarmor.MODID + ":" + name);
        item.setRegistryName(Rarmor.MODID, name);
        GameRegistry.register(item);
    }

}
