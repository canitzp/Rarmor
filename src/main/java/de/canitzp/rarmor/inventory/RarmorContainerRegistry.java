package de.canitzp.rarmor.inventory;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RarmorContainerRegistry {
    
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, RarmorAPI.MOD_ID);

    public static final RegistryObject<MenuType<ContainerRarmor>> RARMOR_CONTAINER = MENU_TYPES.register("rarmor", () -> IForgeMenuType.create(ContainerRarmor::create));

}
