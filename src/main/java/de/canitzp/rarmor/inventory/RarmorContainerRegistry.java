package de.canitzp.rarmor.inventory;

import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RarmorContainerRegistry {
    
    public static final DeferredRegister<ContainerType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, RarmorAPI.MOD_ID);
    
    public static final RegistryObject<ContainerType<ContainerRarmor>> RARMOR_CONTAINER = REGISTRY.register("rarmor", () -> new ContainerType<>(ContainerRarmor::new));

}
