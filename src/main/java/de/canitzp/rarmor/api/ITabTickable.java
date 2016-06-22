package de.canitzp.rarmor.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public interface ITabTickable extends IRarmorTab{

    void tick(World world, EntityPlayer player, ItemStack stack);

}
