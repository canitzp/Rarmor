package de.canitzp.rarmor.data;

import net.minecraftforge.common.capabilities.*;

public class RarmorWorldCapability {

    public static Capability<WorldData> INSTANCE = CapabilityManager.get(new CapabilityToken<>(){});

    public static void registerCapabilityEvent(RegisterCapabilitiesEvent event){
        event.register(WorldData.class);
    }

}
