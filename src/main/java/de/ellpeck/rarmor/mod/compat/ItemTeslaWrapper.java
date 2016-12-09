/*
 * This file ("ItemTeslaWrapper.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.compat;

import cofh.api.energy.IEnergyContainerItem;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemTeslaWrapper implements ITeslaProducer, ITeslaHolder, ITeslaConsumer, ICapabilityProvider{

    private final ItemStack stack;
    private final IEnergyContainerItem item;

    public ItemTeslaWrapper(ItemStack stack, IEnergyContainerItem item){
        this.stack = stack;
        this.item = item;
    }

    @Override
    public long givePower(long power, boolean simulated){
        return this.item.receiveEnergy(this.stack, (int)power, simulated);
    }

    @Override
    public long getStoredPower(){
        return this.item.getEnergyStored(this.stack);
    }

    @Override
    public long getCapacity(){
        return this.item.getMaxEnergyStored(this.stack);
    }

    @Override
    public long takePower(long power, boolean simulated){
        return this.item.extractEnergy(this.stack, (int)power, simulated);
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
