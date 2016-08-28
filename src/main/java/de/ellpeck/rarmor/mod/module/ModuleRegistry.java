/*
 * This file ("ModuleRegistry.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.module;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.mod.module.ender.ActiveModuleEnder;
import de.ellpeck.rarmor.mod.module.furnace.ActiveModuleFurnace;
import de.ellpeck.rarmor.mod.module.generator.ActiveModuleGenerator;
import de.ellpeck.rarmor.mod.module.main.ActiveModuleMain;
import de.ellpeck.rarmor.mod.module.protection.ActiveModuleProtection;
import de.ellpeck.rarmor.mod.module.solar.ActiveModuleSolar;
import de.ellpeck.rarmor.mod.module.storage.ActiveModuleStorage;

public final class ModuleRegistry{

    public static void init(){
        RarmorAPI.registerRarmorModule(ActiveModuleMain.IDENTIFIER, ActiveModuleMain.class);
        RarmorAPI.registerRarmorModule(ActiveModuleStorage.IDENTIFIER, ActiveModuleStorage.class);
        RarmorAPI.registerRarmorModule(ActiveModuleEnder.IDENTIFIER, ActiveModuleEnder.class);
        RarmorAPI.registerRarmorModule(ActiveModuleFurnace.IDENTIFIER, ActiveModuleFurnace.class);
        RarmorAPI.registerRarmorModule(ActiveModuleSolar.IDENTIFIER, ActiveModuleSolar.class);
        RarmorAPI.registerRarmorModule(ActiveModuleGenerator.IDENTIFIER, ActiveModuleGenerator.class);

        RarmorAPI.registerRarmorModule(ActiveModuleProtection.IDENTIFIERS[0], ActiveModuleProtection.Leather.class);
        RarmorAPI.registerRarmorModule(ActiveModuleProtection.IDENTIFIERS[1], ActiveModuleProtection.Chain.class);
        RarmorAPI.registerRarmorModule(ActiveModuleProtection.IDENTIFIERS[2], ActiveModuleProtection.Iron.class);
        RarmorAPI.registerRarmorModule(ActiveModuleProtection.IDENTIFIERS[3], ActiveModuleProtection.Gold.class);
        RarmorAPI.registerRarmorModule(ActiveModuleProtection.IDENTIFIERS[4], ActiveModuleProtection.Diamond.class);
    }

}
