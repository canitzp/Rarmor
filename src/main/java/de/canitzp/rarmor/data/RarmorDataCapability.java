package de.canitzp.rarmor.data;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class RarmorDataCapability {

    public static Capability<RarmorData> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    public static void registerCapabilityEvent(RegisterCapabilitiesEvent event){
        event.register(RarmorData.class);
    }

}
