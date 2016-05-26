/*
 * This file 'ActuallyAdditionsIntegration.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.integration;

import de.canitzp.rarmor.api.Colors;
import de.canitzp.rarmor.items.ItemRegistry;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ActuallyAdditionsIntegration {

    public static void postInit(FMLPostInitializationEvent event) {
        /*de.ellpeck.actuallyadditions.api.recipe.IColorLensChanger lensChanger = new de.ellpeck.actuallyadditions.api.recipe.IColorLensChanger() {
            @Override
            public ItemStack modifyItem(ItemStack itemStack, IBlockState iBlockState, BlockPos blockPos, de.ellpeck.actuallyadditions.api.internal.IAtomicReconstructor iAtomicReconstructor) {
                ItemStack stack = itemStack.copy();
                int i = NBTUtil.getInteger(stack, "colorIndex");
                if (i >= Colors.values().length) {
                    i = 1;
                } else {
                    i++;
                }
                Colors c = Colors.values()[i - 1];
                NBTUtil.setInteger(stack, "color", c.colorValue);
                NBTUtil.setInteger(stack, "colorIndex", i);
                NBTUtil.setString(stack, "colorName", c.colorName + " " + c.colorValueName);
                return stack;
            }
        };
        de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI.addReconstructorLensColorChangeItem(ItemRegistry.rarmorChestplate, lensChanger);
        de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI.addReconstructorLensColorChangeItem(ItemRegistry.rarmorBoots, lensChanger);
        de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI.addReconstructorLensColorChangeItem(ItemRegistry.rarmorLeggins, lensChanger);
        de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI.addReconstructorLensColorChangeItem(ItemRegistry.rarmorHelmet, lensChanger);
    */}

}
