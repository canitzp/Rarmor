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

    public static boolean isDevVersion(){
        return Rarmor.VERSION.equals("@VERSION@");
    }

    private static String[] splitVersion(){
        return Rarmor.VERSION.split("-");
    }

    public static String getMcVersion(){
        return splitVersion()[0];
    }

    public static String getMajorModVersion(){
        return splitVersion()[1].substring(1);
    }

    public static boolean canBeStacked(ItemStack stack1, ItemStack stack2){
        return ItemStack.areItemsEqual(stack1, stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }
}
