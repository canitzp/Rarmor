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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
    public void tick(Level world, Entity entity, boolean isWearingHat, boolean isWearingChest, boolean isWearingPants, boolean isWearingShoes){
        if(isWearingChest && isWearingShoes){
            int use = 1;
            if(this.data.getEnergyStored() >= use){
                if(entity instanceof Player){
                    Player player = (Player) entity;
                    if((player.isOnGround() || player.getAbilities().flying) && !player.isInWater()){
                        if(world.isClientSide()){
                            if(player.moveDist > 0){
                                player.moveRelative(0F, new Vec3(1D, 0.075D, 0D)); // TODO this shit doesn't work
                            }
                        } else {
                            if(this.lastPlayerX != player.getX() || this.lastPlayerZ != player.getZ()){
                                if(this.data.getTotalTickedTicks() % 5 == 0){
                                    this.data.extractEnergy(use, false);
                                }
    
                                this.lastPlayerX = player.getX();
                                this.lastPlayerZ = player.getZ();
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