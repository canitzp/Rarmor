package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.RarmorProperties;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.util.EnergyUtil;
import de.canitzp.rarmor.util.JavaUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;


/**
 * @author canitzp
 */
public class ItemModuleDefense extends ItemModule implements IRarmorModule{

    public int damageMultiplier;

    public ItemModuleDefense(){
        super("moduleDefense");
        this.damageMultiplier = RarmorProperties.getInteger("moduleDefenseDamageMultiplier");
    }

    @Override
    public String getUniqueName(){
        return "Defense";
    }

    @Override
    public List<String> getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return JavaUtil.newList("This Module save you for Damage,",
                "aslong you have enough Energy.",
                "The higher the damage multiplier,",
                "the more energy is needed.",
                TextFormatting.DARK_AQUA + "Fall Damage:  " + TextFormatting.GRAY + "Nearly full absorption.",
                TextFormatting.DARK_AQUA + "Fire Damage:  " + TextFormatting.GRAY + "Nearly full absorption.",
                TextFormatting.DARK_AQUA + "Lava Damage:  " + TextFormatting.GRAY + "Nearly full absorption. Double energy is required.",
                TextFormatting.DARK_AQUA + "Lost in Void: " + TextFormatting.GRAY + "Port you back to Spawn Location if you have more than " + TextFormatting.RED + "50000" + TextFormatting.GRAY + "RF.");
    }

    @Override
    public ModuleType getModuleType(){
        return ModuleType.PASSIVE;
    }

    /**
     * @param world           The World of the Player
     * @param player          The Player itself
     * @param armorChestplate The Rarmor Chestplate
     * @param damageSource    The Type of Damage the Player take
     * @param damage          The Amount of Damage th Player take
     * @return true if you want to cancel the Damage
     */
    @Override
    public boolean onPlayerTakeDamage(World world, EntityPlayer player, ItemStack armorChestplate, DamageSource damageSource, float damage){
        if (EnergyUtil.getEnergy(armorChestplate) >= 50000 && damageSource == DamageSource.outOfWorld){
            if (player.getBedLocation() != null){
                player.setPosition(player.getBedLocation().getX(), player.getBedLocation().getY() + 1, player.getBedLocation().getZ());
            }else{
                player.setPosition(world.getSpawnPoint().getX(), world.getSpawnPoint().getY() + 1, world.getSpawnPoint().getZ());
            }
            EnergyUtil.reduceEnergy(armorChestplate, 50000);
            return true;
        }
        int energyYouNeed = (int) (damage * this.damageMultiplier);
        if (EnergyUtil.getEnergy(armorChestplate) >= energyYouNeed){
            if (damageSource == DamageSource.fall){
                EnergyUtil.reduceEnergy(armorChestplate, energyYouNeed);
                return true;
            }
            if (damageSource == DamageSource.onFire){
                EnergyUtil.reduceEnergy(armorChestplate, energyYouNeed);
                return true;
            }
        }else if (EnergyUtil.getEnergy(armorChestplate) >= energyYouNeed * 2){
            if (damageSource == DamageSource.lava){
                EnergyUtil.reduceEnergy(armorChestplate, energyYouNeed * 2);
                return true;
            }
        }
        return false;
    }
}