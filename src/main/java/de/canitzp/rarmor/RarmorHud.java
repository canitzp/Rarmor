/*
 * This file 'RarmorHud.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import de.canitzp.rarmor.api.hudExtensions.IAdvancedHud;
import de.canitzp.rarmor.items.rfarmor.ArmorHud;
import de.canitzp.rarmor.util.ColorUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class RarmorHud implements IAdvancedHud {

    @Override
    public float onShow(FontRenderer fontRenderer, ScaledResolution resolution, World world, RayTraceResult trace, IBlockState state, TileEntity tileEntity, float x, float y){
        //Forge Fluids:
        if(tileEntity instanceof IFluidHandler){
            IFluidHandler fluid = (IFluidHandler) tileEntity;
            for(int i = 0; i < fluid.getTankInfo(trace.sideHit).length; i++){
                FluidStack fluidStack = fluid.getTankInfo(trace.sideHit)[i].fluid;
                if(fluidStack != null){
                    if(y / 10 > 4){
                        ArmorHud.drawCenteredTextInWorld(fontRenderer, x, y, "To many Fluids to show!", ColorUtil.WHITE.colorValue);
                        y += 10;
                        i = fluid.getTankInfo(trace.sideHit).length;
                        continue;
                    }
                    String fluidText = I18n.translateToLocal(fluidStack.getLocalizedName()) + " " + String.valueOf(fluidStack.amount) + "mB";
                    ArmorHud.drawCenteredTextInWorld(fontRenderer, x, y, fluidText, ColorUtil.WHITE.colorValue);
                    ArmorHud.drawTank((x + fontRenderer.getStringWidth(fluidText)) / 2 + 1, y, fluidStack, fluid.getTankInfo(trace.sideHit)[i].capacity);
                    y += 10;
                }
            }
        }

        //CoFH Energy:
        if(tileEntity instanceof IEnergyStorage){
            IEnergyStorage energy = (IEnergyStorage) tileEntity;
            if(energy.getMaxEnergyStored() > 1){
                String energyText = energy.getEnergyStored() + "/" + energy.getMaxEnergyStored() + "RF";
                ArmorHud.drawCenteredTextInWorld(fontRenderer, x, y, energyText, ColorUtil.WHITE.colorValue);
                ArmorHud.drawBat((x + fontRenderer.getStringWidth(energyText)) / 2 + 1, y, energy.getMaxEnergyStored(), energy.getEnergyStored());
                y += 10;
            }
        } else if(tileEntity instanceof IEnergyReceiver){
            IEnergyReceiver energy = (IEnergyReceiver) tileEntity;
            if(energy.getMaxEnergyStored(trace.sideHit) > 1){
                String energyText = energy.getEnergyStored(trace.sideHit) + "/" + energy.getMaxEnergyStored(trace.sideHit) + "RF";
                ArmorHud.drawCenteredTextInWorld(fontRenderer, x, y, energyText, ColorUtil.WHITE.colorValue);
                ArmorHud.drawBat((x + fontRenderer.getStringWidth(energyText)) / 2 + 1, y, energy.getMaxEnergyStored(trace.sideHit), energy.getEnergyStored(trace.sideHit));
                y += 10;
            }
        } else if(tileEntity instanceof IEnergyProvider){
            IEnergyProvider energy = (IEnergyProvider) tileEntity;
            if(energy.getMaxEnergyStored(trace.sideHit) > 1){
                String energyText = energy.getEnergyStored(trace.sideHit) + "/" + energy.getMaxEnergyStored(trace.sideHit) + "RF";
                ArmorHud.drawCenteredTextInWorld(fontRenderer, x, y, energyText, ColorUtil.WHITE.colorValue);
                ArmorHud.drawBat((x + fontRenderer.getStringWidth(energyText)) / 2 + 1, y, energy.getMaxEnergyStored(trace.sideHit), energy.getEnergyStored(trace.sideHit));
                y += 10;
            }
        }
        return y;
    }

}
