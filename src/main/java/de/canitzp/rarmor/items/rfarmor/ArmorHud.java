package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.RarmorResources;
import de.canitzp.rarmor.api.hudExtensions.IAdvancedHud;
import de.canitzp.rarmor.util.ColorUtil;
import de.canitzp.rarmor.util.GuiUtil;
import de.canitzp.rarmor.util.MinecraftUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class ArmorHud {

    private static final ResourceLocation guiBat = RarmorResources.BATTERYGUI.getNewLocation();
    private static final ResourceLocation guiRarmor = RarmorResources.RARMORGUI.getNewLocation();
    private static FontRenderer fontRenderer = MinecraftUtil.getFontRenderer();

    public static void display(Minecraft mc, ScaledResolution resolution, EntityPlayer player, float x, float y) {
        x += resolution.getScaledWidth();
        RayTraceResult trace = mc.objectMouseOver;
        FontRenderer fontRenderer = mc.fontRendererObj;
        World world = player.getEntityWorld();
        if (trace != null) {
            if (trace.typeOfHit == RayTraceResult.Type.BLOCK || trace.typeOfHit == RayTraceResult.Type.MISS) {
                IBlockState state = world.getBlockState(trace.getBlockPos());
                TileEntity tile = world.getTileEntity(trace.getBlockPos());
                if (state != null) {
                    if (state.getBlock() != null) {
                        if (tile != null) {
                            y = displayBlock(state, x, y);
                            for (IAdvancedHud hud : RarmorAPI.hudExtension) {
                                y = hud.onShow(fontRenderer, resolution, world, trace, state, tile, x, y);
                            }
                        } else if (state.getBlock() instanceof BlockLiquid || state.getBlock() instanceof BlockFluidBase) {
                            drawCenteredTextInWorld(fontRenderer, x, y, state.getBlock().getLocalizedName(), ColorUtil.WHITE);
                        } else {
                            y = displayBlock(state, x, y);
                        }
                    }
                }
            } else if (trace.typeOfHit == RayTraceResult.Type.ENTITY) {
                drawCenteredTextInWorld(fontRenderer, x, y, trace.entityHit.getName(), ColorUtil.WHITE);
            }
        }
    }

    private static float displayBlock(IBlockState state, float x, float y) {
        if (state.getBlock() != Blocks.AIR) {
            Item itemBlock = Item.getItemFromBlock(state.getBlock());
            if (itemBlock != null) {
                drawCenteredTextInWorld(fontRenderer, x, y, itemBlock.getItemStackDisplayName(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state))), ColorUtil.WHITE);
            } else {
                drawCenteredTextInWorld(fontRenderer, x, y, state.getBlock().getLocalizedName(), ColorUtil.WHITE);
            }
        }
        return y + 10;
    }

    public static void drawBat(float x, float y, int maxEnergy, int energyAmount) {
        GuiUtil.drawTexturedModalRect(guiBat, guiRarmor, (int) x, (int) (y - 1), 10, 0, 21, 10);
        GuiUtil.drawBarVertical(guiBat, guiRarmor, (int) x, (int) (y - 1 + 10), 10, 20, 20, 10, maxEnergy, energyAmount);
    }

    public static void drawTank(float x, float y, FluidStack fluid, int maxTank) {
        int factor = (fluid.amount * 20) / maxTank;
        GuiUtil.drawFluid(fluid.getFluid(), (int) x, (int) y - 1, 16, 512 + 10, factor + 1, 10);
        GuiUtil.drawTexturedModalRect(guiBat, guiRarmor, (int) x, (int) y - 1, 31, 21, 21, 10);
    }

    public static void drawCenteredTextInWorld(FontRenderer fontRenderer, float x, float y, String text, int color) {
        fontRenderer.drawStringWithShadow(text, (x - fontRenderer.getStringWidth(text)) / 2, y, color);
    }
}
