package de.canitzp.util.items;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.inventory.GuiHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author canitzp
 */
public class ItemBase extends Item {

    public ItemBase(String modid, String name, CreativeTabs tabs){
        setUnlocalizedName(modid + "." + name);
        setCreativeTab(tabs);
        GameRegistry.registerItem(this, name);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        player.openGui(Rarmor.instance, GuiHandler.DigitalManualGui, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        return stack;
    }

}
