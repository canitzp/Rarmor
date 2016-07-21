package de.canitzp.rarmor.integration;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Registry;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.armor.RarmorColoringTab;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.internal.IAtomicReconstructor;
import de.ellpeck.actuallyadditions.api.recipe.IColorLensChanger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

/**
 * @author canitzp
 */
public class ActuallyAdditions {

    public static void init(){
        IColorLensChanger changer = new IColorLensChanger() {
            @Override
            public ItemStack modifyItem(ItemStack itemStack, IBlockState iBlockState, BlockPos blockPos, IAtomicReconstructor iAtomicReconstructor) {
                ItemStack stack = itemStack.copy();
                Random rnd = iAtomicReconstructor.getWorldObject().rand;
                int rndValue = MathHelper.getRandomIntegerInRange(rnd, 0, RarmorAPI.registerColor.size() - 1);
                int hex = (int) RarmorAPI.registerColor.keySet().toArray()[rndValue];
                NBTUtil.setColor(stack, new RarmorColoringTab.Color(hex, RarmorAPI.registerColor.get(hex)));
                return stack;
            }
        };
        ActuallyAdditionsAPI.addReconstructorLensColorChangeItem(Registry.rarmorHelmet, changer);
        ActuallyAdditionsAPI.addReconstructorLensColorChangeItem(Registry.rarmorChestplate, changer);
        ActuallyAdditionsAPI.addReconstructorLensColorChangeItem(Registry.rarmorLeggins, changer);
        ActuallyAdditionsAPI.addReconstructorLensColorChangeItem(Registry.rarmorBoots, changer);
    }

}
