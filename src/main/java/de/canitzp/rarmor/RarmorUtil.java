package de.canitzp.rarmor;

import de.canitzp.rarmor.armor.ItemRarmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;


/**
 * @author canitzp
 */
public class RarmorUtil{

    public static boolean isPlayerWearingArmor(EntityPlayer player){
        return getRarmorChestplate(player) != null && getRarmorChestplate(player).getItem() instanceof ItemRarmor;
    }

    public static ItemStack getPlayerArmorPart(EntityPlayer player, EntityEquipmentSlot slot){
        return player.inventory.armorInventory[slot.getIndex()];
    }

    public static ItemStack getRarmorChestplate(EntityPlayer player){
        return getPlayerArmorPart(player, EntityEquipmentSlot.CHEST);
    }

    /**
     * Thanks to Ellpeck who allowed me to use this method.
     * https://github.com/Ellpeck/ActuallyAdditions/blob/master/src/main/java/de/ellpeck/actuallyadditions/mod/util/AssetUtil.java#L71
     */
    @SideOnly(Side.CLIENT)
    public static void renderStackToGui(ItemStack stack, int x, int y, float scale){
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);
        Minecraft mc = Minecraft.getMinecraft();
        boolean flagBefore = mc.fontRendererObj.getUnicodeFlag();
        mc.fontRendererObj.setUnicodeFlag(false);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, null);
        mc.fontRendererObj.setUnicodeFlag(flagBefore);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public static void tryToRemap(EntityPlayer player){
        if(!player.getEntityWorld().isRemote){
            ItemStack stack = getRarmorChestplate(player);
            NBTTagCompound nbt = NBTUtil.getTagFromStack(stack);
            if(nbt.hasKey("isFirstOpened")){
                System.out.println("remap");
                NBTTagList list = nbt.getTagList("Items", 10);
                if(!list.hasNoTags()){
                    InventoryBasic inventoryBasic = new InventoryBasic("Rarmor Inventory Tab", false, 63);
                    for(int i = 0; i < list.tagCount(); i++){
                        NBTTagCompound tagCompound = list.getCompoundTagAt(i);
                        byte slotIndex = tagCompound.getByte("Slot");
                        if(slotIndex >= 0 && slotIndex < inventoryBasic.getSizeInventory()){
                            inventoryBasic.setInventorySlotContents(slotIndex, ItemStack.loadItemStackFromNBT(tagCompound));
                        }
                    }
                    nbt.removeTag("Items");
                    nbt.removeTag("isFirstOpened");
                    NBTTagCompound invNBT;
                    NBTUtil.writeInventory(invNBT = new NBTTagCompound(), inventoryBasic);
                    nbt.merge(invNBT);
                    NBTUtil.setTagFromStack(stack, nbt);
                    ((EntityPlayerMP)player).connection.sendPacket(new SPacketSetSlot(-2, 38, stack));
                }
            }
        }
    }

}
