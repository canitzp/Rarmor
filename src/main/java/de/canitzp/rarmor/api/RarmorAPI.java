/*
 * This file 'RarmorAPI.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

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
