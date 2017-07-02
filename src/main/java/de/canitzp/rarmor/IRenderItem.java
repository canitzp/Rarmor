package de.canitzp.rarmor;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public interface IRenderItem {

    @SideOnly(Side.CLIENT)
    void initModel();

}
