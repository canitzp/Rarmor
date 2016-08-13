/*
 * This file ("ItemRegistry.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.mod.item;

import de.canitzp.rarmor.mod.module.storage.ItemModuleStorage;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public final class ItemRegistry{

    public static Item itemRarmorHelmet;
    public static Item itemRarmorChest;
    public static Item itemRarmorPants;
    public static Item itemRarmorBoots;

    public static Item itemModuleStorage;

    public static void preInit(){
        itemRarmorHelmet = new ItemRarmor("itemRarmorHelmet", EntityEquipmentSlot.HEAD);
        itemRarmorChest = new ItemRarmor("itemRarmorChest", EntityEquipmentSlot.CHEST);
        itemRarmorPants = new ItemRarmor("itemRarmorPants", EntityEquipmentSlot.LEGS);
        itemRarmorBoots = new ItemRarmor("itemRarmorBoots", EntityEquipmentSlot.FEET);

        itemModuleStorage = new ItemModuleStorage("itemModuleStorage");
    }

}
