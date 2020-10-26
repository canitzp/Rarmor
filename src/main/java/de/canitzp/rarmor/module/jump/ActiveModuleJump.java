/*
 * This file ("ActiveModuleJump.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.jump;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class ActiveModuleJump extends ActiveRarmorModule {

    public static final ItemStack RABBIT_FOOT = new ItemStack(Items.RABBIT_FOOT);
    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Jump";

    public ActiveModuleJump(IRarmorData data){
        super(data);
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public ItemStack getDisplayIcon(){
        return RABBIT_FOOT;
    }

    @Override
    public void tick(World world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        if(!isWearingChest || !isWearingShoes){
            this.invalid = true;
        }
    }

}
