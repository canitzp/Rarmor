package de.canitzp.rarmor.items.rfarmor;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import com.google.common.collect.Lists;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.util.util.GuiUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;

/**
 * @author canitzp
 */
public class ArmorHud {

    public static void displayNames(Minecraft minecraft, ScaledResolution resolution, EntityPlayer player, float x, float y, int colorCode){
        MovingObjectPosition posHit = Minecraft.getMinecraft().objectMouseOver;
        if(posHit != null){
            if(posHit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK || posHit.typeOfHit == MovingObjectPosition.MovingObjectType.MISS){
                IBlockState currentBlockState = player.getEntityWorld().getBlockState(posHit.getBlockPos());
                if(currentBlockState != null) {
                    Block currentBlock = currentBlockState.getBlock();
                    TileEntity tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(posHit.getBlockPos());
                    if(currentBlock != null && currentBlock.hasTileEntity(currentBlockState)){
                        displayTileEntity(minecraft,resolution, player, x, y, currentBlock, currentBlockState, tileEntity, colorCode);
                    } else if(currentBlock != null && (currentBlock instanceof BlockLiquid || currentBlock instanceof BlockFluidBase)){
                        displayAlternativeBlocks(minecraft, resolution, player, currentBlock, colorCode);
                    } else if(currentBlock != null && currentBlock != Blocks.air){
                        displayStaticBlocks(minecraft, resolution,  player, x, y, currentBlock, currentBlockState, colorCode);
                    }
                }
            } else if(posHit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY){
                String text = posHit.entityHit.getName();
                minecraft.fontRendererObj.drawStringWithShadow(text, (resolution.getScaledWidth() - minecraft.fontRendererObj.getStringWidth(text)) / 2, y , colorCode);
            }
        }
    }

    public static void displayStaticBlocks(Minecraft minecraft, ScaledResolution resolution, EntityPlayer player, float x, float y, Block block, IBlockState state, int colorCode){
        if (player.getEntityWorld().isRemote) {
            Item item = Item.getItemFromBlock(berhlock);
            if(item != null){
                String text = item.getItemStackDisplayName(new ItemStack(block, 1, block.getMetaFromState(state)));
                if(text != null){
                    FontRenderer fontRenderer = minecraft.fontRendererObj;
                    MovingObjectPosition posHit = minecraft.objectMouseOver;
                    if(posHit != null){
                        fontRenderer.drawStringWithShadow(text, (resolution.getScaledWidth() - fontRenderer.getStringWidth(text)) / 2, y , colorCode);
                    }
                }
            } else {
                displayAlternativeBlocks(minecraft, resolution, player, block, colorCode);
            }
        }
    }

    public static void displayAlternativeBlocks(Minecraft minecraft, ScaledResolution resolution, EntityPlayer player, Block block, int colorCode) {
        if (player.getEntityWorld().isRemote) {
            String text = StatCollector.translateToLocal(block.getLocalizedName());
            if (text != null) {
                FontRenderer fontRenderer = minecraft.fontRendererObj;
                MovingObjectPosition posHit = minecraft.objectMouseOver;
                if (posHit != null) {
                    fontRenderer.drawStringWithShadow(text, (resolution.getScaledWidth() - fontRenderer.getStringWidth(text)) / 2, 5, colorCode);
                }
            }
        }
    }

    public static void displayTileEntity(Minecraft minecraft, ScaledResolution resolution, EntityPlayer player, float x, float y, Block block, IBlockState state, TileEntity tileEntity, int colorCode) {
        if (player.getEntityWorld().isRemote) {
            Item item = Item.getItemFromBlock(block);
            if (item != null) {
                String text = item.getItemStackDisplayName(new ItemStack(block, 1, block.getMetaFromState(state)));
                if (text != null) {
                    FontRenderer fontRenderer = minecraft.fontRendererObj;
                    MovingObjectPosition posHit = minecraft.objectMouseOver;
                    if (posHit != null) {
                        drawCenteredTextInWorld(fontRenderer, resolution.getScaledWidth(), y, text, colorCode);
                        float row = 10;
                        if (tileEntity instanceof IFluidHandler) {
                            IFluidHandler fluid = (IFluidHandler) tileEntity;
                            for (int i = 0; i < fluid.getTankInfo(posHit.sideHit).length; i++) {
                                FluidStack fluidStack = fluid.getTankInfo(posHit.sideHit)[i].fluid;
                                if (fluidStack != null) {
                                    if(row / 10 > 4) {
                                        drawCenteredTextInWorld(fontRenderer, resolution.getScaledWidth(), y + row, "To many Fluids to show!", colorCode);
                                        row += 10;
                                        i = fluid.getTankInfo(posHit.sideHit).length;
                                        continue;
                                    }
                                    String fluidText = StatCollector.translateToLocal(fluidStack.getLocalizedName()) + " " + String.valueOf(fluidStack.amount) + "mB";
                                    drawCenteredTextInWorld(fontRenderer, resolution.getScaledWidth(), y + row, fluidText, colorCode);
                                    drawTank(fontRenderer, resolution, fluidText, row, y, fluidStack, fluid.getTankInfo(posHit.sideHit)[i].capacity);
                                    row += 10;
                                }
                            }
                        }
                        if (tileEntity instanceof IEnergyStorage) {
                            IEnergyStorage energy = (IEnergyStorage) tileEntity;
                            if (energy.getMaxEnergyStored() > 1) {
                                String energyText = energy.getEnergyStored() + "/" + energy.getMaxEnergyStored() + "RF";
                                drawCenteredTextInWorld(fontRenderer, resolution.getScaledWidth(), y + row, energyText, colorCode);
                                drawBat(fontRenderer, resolution, energyText, row, y, energy.getMaxEnergyStored(), energy.getEnergyStored());
                                row += 10;
                            }
                        } else if (tileEntity instanceof IEnergyReceiver) {
                            IEnergyReceiver energy = (IEnergyReceiver) tileEntity;
                            if (energy.getMaxEnergyStored(posHit.sideHit) > 1) {
                                String energyText = energy.getEnergyStored(posHit.sideHit) + "/" + energy.getMaxEnergyStored(posHit.sideHit) + "RF";
                                drawCenteredTextInWorld(fontRenderer, resolution.getScaledWidth(), y + row, energyText, colorCode);
                                drawBat(fontRenderer, resolution, energyText, row, y, energy.getMaxEnergyStored(posHit.sideHit), energy.getEnergyStored(posHit.sideHit));
                                row += 10;
                            }
                        } else if (tileEntity instanceof IEnergyProvider) {
                            IEnergyProvider energy = (IEnergyProvider) tileEntity;
                            if (energy.getMaxEnergyStored(posHit.sideHit) > 1) {
                                String energyText = energy.getEnergyStored(posHit.sideHit) + "/" + energy.getMaxEnergyStored(posHit.sideHit) + "RF";
                                drawCenteredTextInWorld(fontRenderer, resolution.getScaledWidth(), y + row, energyText, colorCode);
                                drawCenteredTextInWorld(fontRenderer, resolution.getScaledWidth(), y + row, energy.getEnergyStored(posHit.sideHit) + "/" + energy.getMaxEnergyStored(posHit.sideHit) + "RF", colorCode);
                                drawBat(fontRenderer, resolution, energyText, row, y, energy.getMaxEnergyStored(posHit.sideHit), energy.getEnergyStored(posHit.sideHit));
                                row += 10;
                            }
                        }
                    }
                }
            } else {
                displayAlternativeBlocks(minecraft, resolution, player, block, colorCode);
            }
        }
    }

    private static void drawBat(FontRenderer fontRenderer, ScaledResolution resolution, String energyText, float row, float y, int maxEnergy, int energyAmount){
        ResourceLocation guiBat = new ResourceLocation(Rarmor.MODID, "textures/gui/guiBattery.png");
        ResourceLocation normalGui = new ResourceLocation(Rarmor.MODID, "textures/gui/guiRFArmorNormal.png");
        int xg = ((resolution.getScaledWidth() - fontRenderer.getStringWidth(energyText)) / 2) + fontRenderer.getStringWidth(energyText) + 1;
        GuiUtil.drawTexturedModalRect(guiBat, normalGui, xg, (int) ((int) row + y) - 1, 10, 0, 21, 10);
        GuiUtil.drawBarVertical(guiBat, normalGui, xg, (int) ((int) row + y) - 1 + 10, 10, 20, 20, 10, maxEnergy, energyAmount);
    }

    private static void drawTank(FontRenderer fontRenderer, ScaledResolution resolution, String energyText, float row, float y, FluidStack fluid, int maxTank){
        ResourceLocation guiBat = new ResourceLocation(Rarmor.MODID, "textures/gui/guiBattery.png");
        ResourceLocation normalGui = new ResourceLocation(Rarmor.MODID, "textures/gui/guiRFArmorNormal.png");
        int xg = ((resolution.getScaledWidth() - fontRenderer.getStringWidth(energyText)) / 2) + fontRenderer.getStringWidth(energyText) + 1;
        int factor = (fluid.amount * 20) / maxTank;
        GuiUtil.drawFluid(fluid.getFluid(), xg, (int) ((int) row + y) - 1, 16, 512 + 10, factor + 1, 10);
        GuiUtil.drawTexturedModalRect(guiBat, normalGui, xg, (int) ((int) row + y) - 1, 31, 21, 21, 10);
    }

    public static void drawCenteredTextInWorld(FontRenderer fontRenderer, float x, float y, String text, int color){
        fontRenderer.drawStringWithShadow(text, (x - fontRenderer.getStringWidth(text)) / 2, y, color);
    }
}
