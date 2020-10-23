/*
 * This file ("ModuleRegistry.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.module.color.ActiveModuleColor;
import de.canitzp.rarmor.module.ender.ActiveModuleEnder;
import de.canitzp.rarmor.module.furnace.ActiveModuleFurnace;
import de.canitzp.rarmor.module.generator.ActiveModuleGenerator;
import de.canitzp.rarmor.module.main.ActiveModuleMain;
import de.canitzp.rarmor.module.protection.ActiveModuleProtectionGold;
import de.canitzp.rarmor.module.protection.ActiveModuleProtectionIron;
import de.canitzp.rarmor.module.solar.ActiveModuleSolar;
import de.canitzp.rarmor.module.speed.ActiveModuleSpeed;
import de.canitzp.rarmor.module.storage.ActiveModuleStorage;
import de.canitzp.rarmor.module.jump.ActiveModuleJump;
import de.canitzp.rarmor.module.protection.ActiveModuleProtectionDiamond;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public final class ModuleRegistry{
    
    public static void init(){
        RarmorAPI.registerRarmorModule(ActiveModuleMain.IDENTIFIER, ActiveModuleMain.class);
        RarmorAPI.registerRarmorModule(ActiveModuleColor.IDENTIFIER, ActiveModuleColor.class);

        RarmorAPI.registerRarmorModule(ActiveModuleStorage.IDENTIFIER, ActiveModuleStorage.class);
        RarmorAPI.registerRarmorModule(ActiveModuleEnder.IDENTIFIER, ActiveModuleEnder.class);
        RarmorAPI.registerRarmorModule(ActiveModuleFurnace.IDENTIFIER, ActiveModuleFurnace.class);
        RarmorAPI.registerRarmorModule(ActiveModuleSolar.IDENTIFIER, ActiveModuleSolar.class);
        RarmorAPI.registerRarmorModule(ActiveModuleGenerator.IDENTIFIER, ActiveModuleGenerator.class);
        RarmorAPI.registerRarmorModule(ActiveModuleSpeed.IDENTIFIER, ActiveModuleSpeed.class);
        RarmorAPI.registerRarmorModule(ActiveModuleJump.IDENTIFIER, ActiveModuleJump.class);

        RarmorAPI.registerRarmorModule(ActiveModuleProtectionIron.IDENTIFIER, ActiveModuleProtectionIron.class);
        RarmorAPI.registerRarmorModule(ActiveModuleProtectionGold.IDENTIFIER, ActiveModuleProtectionGold.class);
        RarmorAPI.registerRarmorModule(ActiveModuleProtectionDiamond.IDENTIFIER, ActiveModuleProtectionDiamond.class);
    }

}
