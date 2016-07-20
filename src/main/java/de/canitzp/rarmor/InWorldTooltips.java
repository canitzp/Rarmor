package de.canitzp.rarmor;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import de.canitzp.rarmor.api.RarmorSettings;
import de.canitzp.rarmor.api.RarmorValues;
import de.canitzp.rarmor.api.tooltip.*;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.armor.RarmorColoringTab;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class InWorldTooltips implements IInWorldTooltip {

    private RarmorColoringTab.Color textColor = new RarmorColoringTab.Color(0xFFFFFF, "");

    @SideOnly(Side.CLIENT)
    @Override
    public void showTooltip(WorldClient world, EntityPlayerSP player, ItemStack stack, ScaledResolution resolution, FontRenderer fontRenderer, RenderGameOverlayEvent.ElementType type, float partialTicks, boolean isHelmet) {
        if(RarmorValues.tooltipsAlwaysActive || (isHelmet && RarmorSettings.getSettingBoolean(stack, RarmorSettings.Settings.INWORLDTOOLTIPS))){
            Minecraft mc = Minecraft.getMinecraft();
            RayTraceResult trace = mc.objectMouseOver;
            if(trace != null){
                if(trace.typeOfHit.equals(RayTraceResult.Type.BLOCK) || trace.typeOfHit.equals(RayTraceResult.Type.MISS)){
                    IBlockState state = world.getBlockState(trace.getBlockPos());
                    TileEntity tileEntity = world.getTileEntity(trace.getBlockPos());
                    if(state.getBlock() != Blocks.AIR){
                        List<TooltipComponent> toShow = new ArrayList<>();
                        if(state.getBlock() instanceof BlockLiquid || state.getBlock() instanceof BlockFluidBase){
                            toShow.add(new TooltipComponent().addText(state.getBlock().getLocalizedName()));
                        } else {
                            for(IInWorldTooltip tooltip : RarmorAPI.getInWorldTooltips()){
                                toShow.add(tooltip.showTooltipAtBlock(world, player, stack, resolution, fontRenderer, state, tileEntity, partialTicks, isHelmet));
                            }
                        }
                        this.showList(fontRenderer, resolution.getScaledWidth(), 5, toShow);
                    }
                } else if (trace.typeOfHit.equals(RayTraceResult.Type.ENTITY)){
                    Entity entity = trace.entityHit;
                    if(entity != null){
                        List<TooltipComponent> toShow = new ArrayList<>();
                        for(IInWorldTooltip tooltip : RarmorAPI.getInWorldTooltips()){
                            toShow.add(tooltip.showTooltipAtEntity(world, player, stack, resolution, fontRenderer, entity, partialTicks, isHelmet));
                        }
                        this.showList(fontRenderer, resolution.getScaledWidth(), 5, toShow);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TooltipComponent showTooltipAtBlock(WorldClient world, EntityPlayerSP player, ItemStack stack, ScaledResolution resolution, FontRenderer fontRenderer, IBlockState state, TileEntity tileEntity, float partialTicks, boolean isHelmet) {
        TooltipComponent component = new TooltipComponent().addText(getBlockName(state)).newLine();
        if(tileEntity != null){
            RayTraceResult trace = Minecraft.getMinecraft().objectMouseOver;
            EnumFacing side = trace.sideHit;
            //CoFH Energy:
            if(tileEntity instanceof IEnergyProvider){
                RarmorUtil.requestEnergy(tileEntity.getPos(), side);
                IEnergyProvider tile = (IEnergyProvider) tileEntity;
                int currentEnergy = tile.getEnergyStored(side), maxEnergy = tile.getMaxEnergyStored(side);
                if(maxEnergy > 1){
                    component.addRenderer(new ComponentRendererEnergy(currentEnergy, maxEnergy, "RF"));
                    component.newLine();
                }
            } else if(tileEntity instanceof IEnergyReceiver){
                RarmorUtil.requestEnergy(tileEntity.getPos(), side);
                IEnergyReceiver tile = (IEnergyReceiver) tileEntity;
                int currentEnergy = tile.getEnergyStored(side), maxEnergy = tile.getMaxEnergyStored(side);
                if(maxEnergy > 1){
                    component.addRenderer(new ComponentRendererEnergy(currentEnergy, maxEnergy, "RF"));
                    component.newLine();
                }
            } else if(tileEntity instanceof IEnergyStorage){
                RarmorUtil.requestEnergy(tileEntity.getPos(), side);
                IEnergyStorage tile = (IEnergyStorage) tileEntity;
                int currentEnergy = tile.getEnergyStored(), maxEnergy = tile.getMaxEnergyStored();
                if(maxEnergy > 1){
                    component.addRenderer(new ComponentRendererEnergy(currentEnergy, maxEnergy, "RF"));
                    component.newLine();
                }
            }
            //Forge FluidTanks:
            if(tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)){
                IFluidHandler tile = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
                IFluidTankProperties[] tankProps = tile.getTankProperties();
                if(tankProps != null){
                    for(IFluidTankProperties tank : tankProps){
                        if(tank != null){
                            if(tank.getCapacity() > 0){
                                component.addRenderer(new ComponentRendererTank(tank.getContents(), tank.getCapacity())).newLine();
                            }
                        }
                    }
                }
            } else if(tileEntity instanceof net.minecraftforge.fluids.IFluidHandler){
                component.addText("This Side of the Block uses the old Fluid System.").newLine();
            }
        }
        return component;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TooltipComponent showTooltipAtEntity(WorldClient world, EntityPlayerSP player, ItemStack stack, ScaledResolution resolution, FontRenderer fontRenderer, Entity entity, float partialTicks, boolean isHelmet) {
        return new TooltipComponent().addText(entity.getName()).newLine().addText(entity instanceof EntityLivingBase ? TextFormatting.RED.toString() + ((EntityLivingBase) entity).getHealth() + "/" + ((EntityLivingBase) entity).getMaxHealth() : null);
    }

    @SideOnly(Side.CLIENT)
    public static String getBlockName(IBlockState state){
        Item itemBlock = Item.getItemFromBlock(state.getBlock());
        if(itemBlock != null){
            return itemBlock.getItemStackDisplayName(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)));
        } else {
            return state.getBlock().getLocalizedName();
        }
    }

    @SideOnly(Side.CLIENT)
    private void showList(FontRenderer fontRenderer, int x, int y, List<TooltipComponent> lines){
        for(TooltipComponent tooltipComponent : lines){
            if(tooltipComponent != null){
                for(List<Object> lists : tooltipComponent.endComponent()){
                    if(lists != null){
                        for(Object o : lists){
                            if(o != null){
                                if(o instanceof String){
                                    fontRenderer.drawString((String) o, (x - fontRenderer.getStringWidth((String) o)) / 2, y, this.textColor.hexValue, true);
                                } else if (o instanceof IComponentRender){
                                    ((IComponentRender) o).render(fontRenderer, x, y, this.textColor.hexValue);
                                }
                            }
                        }
                    }
                    y += 10;
                }
            }
        }
    }
}
