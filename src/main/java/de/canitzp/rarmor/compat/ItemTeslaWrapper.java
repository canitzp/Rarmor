/*
 * This file ("ItemTeslaWrapper.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.compat;

import de.canitzp.rarmor.item.ItemRarmorChest;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemTeslaWrapper implements ITeslaProducer, ITeslaHolder, ITeslaConsumer, ICapabilityProvider{

    private final ItemRarmorChest.EStorage storage;

    public ItemTeslaWrapper(ItemRarmorChest.EStorage storage){
       this.storage = storage;
    }

    @Override
    public long givePower(long power, boolean simulated){
        return this.storage.receiveEnergy((int)power, simulated);
    }

    @Override
    public long getStoredPower(){
        return this.storage.getEnergyStored();
    }

    @Override
    public long getCapacity(){
        return this.storage.getMaxEnergyStored();
    }

    @Override
    public long takePower(long power, boolean simulated){
        return this.storage.extractEnergy((int)power, simulated);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing){
        return capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == TeslaCapabilities.CAPABILITY_HOLDER || capability == TeslaCapabilities.CAPABILITY_CONSUMER;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing){
        return this.hasCapability(capability, facing) ? (T)this : null;
    }
}
