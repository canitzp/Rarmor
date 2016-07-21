package de.canitzp.rarmor.integration;

import de.canitzp.rarmor.api.tooltip.ComponentRendererEnergy;
import de.canitzp.rarmor.api.tooltip.IInWorldTooltip;
import de.canitzp.rarmor.api.tooltip.TooltipComponent;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class Tesla implements IInWorldTooltip{

    @Optional.Method(modid = Integration.TESLA)
    @SideOnly(Side.CLIENT)
    @Override
    public TooltipComponent showTooltipAtBlock(WorldClient world, EntityPlayerSP player, @Nullable ItemStack stack, ScaledResolution resolution, FontRenderer fontRenderer, IBlockState state, TileEntity tileEntity, float partialTicks, boolean isHelmet) {
        RayTraceResult trace = Minecraft.getMinecraft().objectMouseOver;
        TileEntity tile = world.getTileEntity(trace.getBlockPos());
        if(tile != null && tile.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, trace.sideHit)){
            ITeslaHolder tesla = tile.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, trace.sideHit);
            if(tesla.getCapacity() > 0){
                return new TooltipComponent().addRenderer(new ComponentRendererEnergy((int)tesla.getStoredPower(), (int)tesla.getCapacity(), "T").setEnergyColor(0x00BFFF));
            }
        }
        return null;
    }
}
