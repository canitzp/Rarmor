/*
 * This file ("Helper.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.canitzp.rarmor.misc;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.internal.IRarmorData;
import de.canitzp.rarmor.api.module.ActiveRarmorModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    public static void renderStackToGui(PoseStack matrixStack, ItemStack stack, double x, double y, float scale){
        if(!stack.isEmpty()){
            Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.pushPose();
            posestack.translate(x, y, 100.0F + Minecraft.getInstance().getItemRenderer().blitOffset);
            posestack.translate(8.0D, 8.0D, 0.0D);
            posestack.scale(1.0F, -1.0F, 1.0F);
            posestack.scale(16.0F, 16.0F, 16.0F);
            posestack.scale(scale, scale, scale);
            RenderSystem.applyModelViewMatrix();
            PoseStack posestack1 = new PoseStack();
            MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();

            BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, (Level) null, (LivingEntity) null, 0);

            boolean flag = !model.usesBlockLight();
            if (flag) {
                Lighting.setupForFlatItems();
            }

            Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.GUI, false, posestack1, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, model);
            multibuffersource$buffersource.endBatch();
            RenderSystem.enableDepthTest();
            if (flag) {
                Lighting.setupFor3DItems();
            }

            posestack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }
    
    public static boolean canBeStacked(ItemStack stack1, ItemStack stack2){
        return ItemStack.isSame(stack1, stack2) && ItemStack.tagMatches(stack1, stack2);
    }

    public static void setItemEnergy(ItemStack stack, int energy){
        if(!stack.hasTag()){
            stack.setTag(new CompoundTag());
        }
        stack.getTag().putInt("Energy", energy);
    }

    @OnlyIn(Dist.CLIENT)
    public static int getRGBDurabilityForDisplay(){
        LocalPlayer player = Minecraft.getInstance().player;
        if(player != null && player.level != null){
            float[] color = getColor(player.level.getGameTime()%256);
            return Mth.hsvToRgb(color[0]/255F, color[1]/255F, color[2]/255F);
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
