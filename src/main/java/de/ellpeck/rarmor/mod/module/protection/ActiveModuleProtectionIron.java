/*
 * This file ("ActiveModuleProtectionIron.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module.protection;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ActiveModuleProtectionIron extends ActiveModuleProtection{

    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"ProtectionIron";

    public ActiveModuleProtectionIron(IRarmorData data){
        super(new ItemStack(Items.IRON_CHESTPLATE), IDENTIFIER, data);
    }

    @Override
    public float getDamageReduction(){
        return 2.5F;
    }
}
