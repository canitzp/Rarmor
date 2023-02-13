/*
 * This file ("ActiveModuleMain.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.main;

import de.canitzp.rarmor.api.inventory.RarmorModuleContainer;
import de.canitzp.rarmor.item.RarmorItemRegistry;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.inventory.RarmorModuleGui;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.inventory.gui.BasicInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;

public class ActiveModuleMain extends ActiveRarmorModule{

    public static final int MODULE_SLOT_AMOUNT = 3;
    public static final String IDENTIFIER = RarmorAPI.MOD_ID+"Main";
    private static final ItemStack CHESTPLATE = new ItemStack(RarmorItemRegistry.itemRarmorChest.get());
    public final SimpleContainer inventory = new SimpleContainer(2);
    private int lastEnergy;

    public ActiveModuleMain(IRarmorData data){
        super(data);
    }

    @Override
    public void tick(Level world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        if(!world.isClientSide()){
            if(this.data.getEnergyStored() < this.data.getMaxEnergyStored()){
                ItemStack discharge = this.inventory.getItem(0);
                if(!discharge.isEmpty()){
                    discharge.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
                        int canDischarge = storage.extractEnergy(Integer.MAX_VALUE, true);
                        if(canDischarge > 0){
                            int discharged = this.data.receiveEnergy(canDischarge, false);
                            storage.extractEnergy(discharged, false);
                            this.data.setDirty();
                        }
                    });
                }
            }

            if(this.data.getEnergyStored() > 0){
                ItemStack charge = this.inventory.getItem(1);
                if(!charge.isEmpty()){
                    charge.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
                        int canDischarge = storage.receiveEnergy(Integer.MAX_VALUE, true);
                        if(canDischarge > 0){
                            int discharged = this.data.extractEnergy(canDischarge, false);
                            storage.receiveEnergy(discharged, false);
                            this.data.setDirty();
                        }
                    });
                }
            }

            int energy = this.data.getEnergyStored();
            if(energy != this.lastEnergy && this.data.getTotalTickedTicks()%10 == 0){
                this.data.queueUpdate();
                this.lastEnergy = energy;
            }
        }
    }
    
    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }

    @Override
    public void readFromNBT(CompoundTag compound, boolean sync){
        if(!sync){
            this.inventory.fromTag(compound.getList("Items", Tag.TAG_COMPOUND));
        }
    }

    @Override
    public void writeToNBT(CompoundTag compound, boolean sync){
        if(!sync){
            compound.put("Items", this.inventory.createTag());
        }
    }

    @Override
    public RarmorModuleContainer createContainer(Player player, AbstractContainerMenu container){
        return new ContainerModuleMain(player, container, this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RarmorModuleGui createGui(){
        return new GuiModuleMain(this);
    }

    @Override
    public boolean hasTab(Player player){
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack getDisplayIcon(){
        return CHESTPLATE;
    }

}
