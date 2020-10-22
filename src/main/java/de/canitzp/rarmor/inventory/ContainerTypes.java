package de.canitzp.rarmor.inventory;

import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypes {
    
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RarmorAPI.MOD_ID);
    
    public static final RegistryObject<ContainerType<ContainerRarmor>> RARMOR_CONTAINER = CONTAINERS.register("rarmor", () -> new ContainerType<>(ContainerRarmor::new));

}
