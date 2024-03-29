/*
 * This file ("ActiveModuleEnder.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.ender;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ActiveModuleEnder extends ActiveRarmorModule{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Ender";
    private static final ItemStack ENDER_CHEST = new ItemStack(Blocks.ENDER_CHEST);

    public ActiveModuleEnder(IRarmorData data){
        super(data);
    }

    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public RarmorModuleContainer createContainer(Player player, AbstractContainerMenu container){
        return new ContainerModuleEnder(player, container, this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RarmorModuleGui createGui(){
        return new GuiModuleEnder(this);
    }

    @Override
    public boolean hasTab(Player player){
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack getDisplayIcon(){
        return ENDER_CHEST;
    }

}
