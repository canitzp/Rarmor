/*
 * This file ("ActiveModuleSpeed.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.module.speed;

import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ActiveModuleSpeed extends ActiveRarmorModule {
    
    private static final ItemStack BOOTS = new ItemStack(Items.GOLDEN_BOOTS);
    public static final String IDENTIFIER = RarmorAPI.MOD_ID + "Speed";
    
    private double lastPlayerX;
    private double lastPlayerZ;
    
    public ActiveModuleSpeed(IRarmorData data){
        super(data);
    }
    
    @Override
    public String getIdentifier(){
        return IDENTIFIER;
    }
    
    @Override
    public ItemStack getDisplayIcon(){
        return BOOTS;
    }
    
    @Override
    public void tick(World world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        if(isWearingChest && isWearingShoes){
            int use = 1;
            if(this.data.getEnergyStored() >= use){
                if(entity instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) entity;
                    if((player.isOnGround() || player.abilities.isFlying) && !player.isInWater()){
                        if(world.isRemote){
                            if(player.moveForward > 0){
                                player.moveRelative(0F, new Vector3d(1F, 0.075F, 0F)); // TODO this shit doesn't work
                            }
                        } else {
                            if(this.lastPlayerX != player.getPosX() || this.lastPlayerZ != player.getPosZ()){
                                if(this.data.getTotalTickedTicks() % 5 == 0){
                                    this.data.extractEnergy(use, false);
                                }
    
                                this.lastPlayerX = player.getPosX();
                                this.lastPlayerZ = player.getPosZ();
                            }
                        }
                    }
                }
            }
        } else {
            this.invalid = true;
        }
    }
    
}