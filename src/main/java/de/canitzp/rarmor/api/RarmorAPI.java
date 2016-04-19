package de.canitzp.rarmor.api;

import de.canitzp.rarmor.api.hudExtensions.IAdvancedHud;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class RarmorAPI{

    @SideOnly(Side.CLIENT)
    public static List<IAdvancedHud> hudExtension = new ArrayList<>();

    @SideOnly(Side.CLIENT)
    public static void addAdvancedHud(IAdvancedHud hud){
        hudExtension.add(hud);
    }

}
