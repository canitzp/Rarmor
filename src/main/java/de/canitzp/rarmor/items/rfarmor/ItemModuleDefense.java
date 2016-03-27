package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.RarmorProperties;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.util.EnergyUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;


/**
 * @author canitzp
 */
public class ItemModuleDefense extends ItemModule implements IRarmorModule{

    public int damageMultiplier;

    public ItemModuleDefense() {
        super("moduleDefense");
        this.damageMultiplier = RarmorProperties.getInteger("moduleDefenseDamageMultiplier");
    }

    @Override
    public String getUniqueName() {
        return "Defense";
    }

    /**
     * @param world        The World of the Player
     * @param player       The Player itself
     * @param armor        The Rarmor Chestplate
     * @param damageSource The Type of Damage the Player take
     * @param damage       The Amount of Damage th Player take
     * @return true if you want to cancel the Damage
     */
    @Override
    public boolean onPlayerTakeDamage(World world, EntityPlayer player, ItemStack armor, DamageSource damageSource, float damage) {
        int energyYouNeed = (int) (damage * this.damageMultiplier);
        if(EnergyUtil.getEnergy(armor) >= energyYouNeed){
            if(damageSource == DamageSource.fall){
                EnergyUtil.reduceEnergy(armor, energyYouNeed);
                return true;
            }
            if(damageSource == DamageSource.onFire){
                EnergyUtil.reduceEnergy(armor, energyYouNeed);
                player.attackEntityFrom(DamageSource.onFire, 5F);
                return true;
            }
        }
        return false;
    }
}
