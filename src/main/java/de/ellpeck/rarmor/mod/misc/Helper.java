/*
 * This file ("Helper.java") is part of the Rarmor mod for Minecraft.
 * It is created by Ellpeck and owned by canitzp & Ellpeck and distributed
 * under the Rarmor License to be found at
 * https://github.com/Ellpeck/Rarmor/blob/master/LICENSE.md
 * View the source code at https://github.com/Ellpeck/Rarmor
 *
 * © 2015-2016 canitzp & Ellpeck
 */

package de.ellpeck.rarmor.mod.misc;

import de.ellpeck.rarmor.api.RarmorAPI;
import de.ellpeck.rarmor.api.internal.IRarmorData;
import de.ellpeck.rarmor.api.module.ActiveRarmorModule;
import de.ellpeck.rarmor.mod.Rarmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public final class Helper{

    public static ActiveRarmorModule initiateModuleById(String id, IRarmorData data){
        if(id != null && !id.isEmpty()){
            Class<? extends ActiveRarmorModule> moduleClass = RarmorAPI.RARMOR_MODULE_REGISTRY.get(id);
            ActiveRarmorModule module = initiateModule(moduleClass, data);

            String moduleId = module.getIdentifier();
            if(!id.equals(moduleId)){
                Rarmor.LOGGER.fatal("A "+Rarmor.MOD_NAME+" Module has a different identifier than the one it was registered with. This is not allowed behavior! Expected id: "+id+", got "+moduleId+".");
            }

            return module;
        }
        return null;
    }

    private static ActiveRarmorModule initiateModule(Class<? extends ActiveRarmorModule> moduleClass, IRarmorData data){
        try{
            return moduleClass.getConstructor(IRarmorData.class).newInstance(data);
        }
        catch(Exception e){
            Rarmor.LOGGER.error("Trying to initiate a module failed!", e);
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public static ResourceLocation getGuiLocation(String guiName){
        return new ResourceLocation(RarmorAPI.MOD_ID, "textures/gui/"+guiName+".png");
    }

    @SideOnly(Side.CLIENT)
    public static void renderStackToGui(ItemStack stack, float x, float y, float scale){
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);

        Minecraft mc = Minecraft.getMinecraft();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, null);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
