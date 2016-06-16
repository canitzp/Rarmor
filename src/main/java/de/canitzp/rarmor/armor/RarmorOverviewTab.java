package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.Registry;
import de.canitzp.rarmor.api.IRarmorTab;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class RarmorOverviewTab implements IRarmorTab{
    @Override
    public String getTabIdentifier(){
        return Rarmor.MODID + ":overviewTab";
    }

    @Override
    public ItemStack getTabIcon(){
        return new ItemStack(Registry.rarmorChestplate);
    }

    @Override
    public String getTabHoveringText(){
        return "Overview";
    }
}
