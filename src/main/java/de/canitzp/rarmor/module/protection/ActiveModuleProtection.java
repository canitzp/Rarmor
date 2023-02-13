/*
 * This file ("ActiveModuleProtection.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.protection;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.world.item.ItemStack;

public abstract class ActiveModuleProtection extends ActiveRarmorModule {

    private final String identifier;
    private final ItemStack display;

    public ActiveModuleProtection(ItemStack display, String identifier, IRarmorData data){
        super(data);
        this.identifier = identifier;
        this.display = display;
    }

    @Override
    public String getIdentifier(){
        return this.identifier;
    }

    @Override
    public ItemStack getDisplayIcon(){
        return this.display;
    }

    public abstract float getDamageReduction();
}
