/*
 * This file ("Helper.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

public final class Helper{

    public static ActiveRarmorModule initiateModuleById(String id, IRarmorData data){
        if(id != null && !id.isEmpty()){
            Class<? extends ActiveRarmorModule> moduleClass = RarmorAPI.RARMOR_MODULE_REGISTRY.get(id);
            if(moduleClass != null){
                ActiveRarmorModule module = initiateModule(moduleClass, data);

                if(module != null){
                    String moduleId = module.getIdentifier();
                    if(!id.equals(moduleId)){
                        Rarmor.LOGGER.fatal("A "+Rarmor.MOD_NAME+" Module has a different identifier than the one it was registered with. This is not allowed behavior! Expected id: "+id+", got "+moduleId+".");
                    }

                    return module;
                }
            }
            else{
                Rarmor.LOGGER.fatal("A "+Rarmor.MOD_NAME+" Module has failed to initialize as it isn't registered. This is a critical error! Id: "+id+".");
            }
        }
        return null;
    }

    private static ActiveRarmorModule initiateModule(Class<? extends ActiveRarmorModule> moduleClass, IRarmorData data){
        try{
            return moduleClass.getConstructor(IRarmorData.class).newInstance(data);
        }
        catch(Exception e){
            Rarmor.LOGGER.fatal("Trying to initiate a "+Rarmor.MOD_NAME+" Module failed! Probably there is no fitting constructor available.", e);
            return null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getGuiLocation(String guiName){
        return new ResourceLocation(RarmorAPI.MOD_ID, "textures/gui/"+guiName+".png");
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderStackToGui(MatrixStack matrixStack, ItemStack stack, float x, float y, float scale){
        if(!stack.isEmpty()){
            matrixStack.push();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.enableStandardItemLighting();
            RenderSystem.enableDepthTest();
            //GlStateManager.enableRescaleNormal();
            matrixStack.translate(x, y, 0);
            matrixStack.scale(scale, scale, scale);

            Minecraft mc = Minecraft.getInstance();
            mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
            mc.getItemRenderer().renderItemOverlayIntoGUI(mc.fontRenderer, stack, 0, 0, null);

            RenderHelper.disableStandardItemLighting();
            matrixStack.pop();
        }
    }
    
    public static boolean canBeStacked(ItemStack stack1, ItemStack stack2){
        return ItemStack.areItemsEqual(stack1, stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    public static void setItemEnergy(ItemStack stack, int energy){
        if(!stack.hasTag()){
            stack.setTag(new CompoundNBT());
        }
        stack.getTag().putInt("Energy", energy);
    }

    @OnlyIn(Dist.CLIENT)
    public static int getRGBDurabilityForDisplay(){
        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null && player.world != null){
            float[] color = getColor(player.world.getGameTime()%256);
            return MathHelper.rgb(color[0]/255F, color[1]/255F, color[2]/255F);
        }
        return 0;
    }

    @OnlyIn(Dist.CLIENT)
    public static float[] getColor(float pos){
        if(pos < 85.0f){
            return new float[]{pos*3.0F, 255.0f-pos*3.0f, 0.0f};
        } else if(pos < 170.0f){
            return new float[]{255.0f-(pos -= 85.0f)*3.0f, 0.0f, pos*3.0f};
        }
        return new float[]{0.0f, (pos -= 170.0f)*3.0f, 255.0f-pos*3.0f};
    }

}
