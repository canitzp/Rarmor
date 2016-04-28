/*
 * This file 'IAdvancedHud.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.api.hudExtensions;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public interface IAdvancedHud{

    float onShow(FontRenderer fontRenderer, ScaledResolution resolution, World world, RayTraceResult trace, IBlockState state, TileEntity tileEntity, float x, float y);

}
