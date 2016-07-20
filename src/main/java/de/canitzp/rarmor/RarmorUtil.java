package de.canitzp.rarmor;

import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.armor.ItemRarmor;
import de.canitzp.rarmor.network.PacketHandler;
import de.canitzp.rarmor.network.PacketRarmorPacketData;
import de.canitzp.rarmor.network.PacketRequestEnergyLevel;
import de.canitzp.rarmor.network.PacketSendBoolean;
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
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
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

    public static ItemStack reduceStacksize(ItemStack stack){
        if(stack.stackSize > 1){
            return new ItemStack(stack.getItem(), stack.getMetadata(), --stack.stackSize);
        } else {
            return null;
        }
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
                Rarmor.logger.info(player.getName() + " has opened the new Rarmor. All of them Items of the old, are dropped to the ground.");
                NBTTagList list = nbt.getTagList("Items", 10);
                if(!list.hasNoTags()){
                    for(int i = 0; i < list.tagCount(); i++){
                        NBTTagCompound tagCompound = list.getCompoundTagAt(i);
                        byte slotIndex = tagCompound.getByte("Slot");
                        if(slotIndex >= 0 && slotIndex < 63){
                            player.dropItem(ItemStack.loadItemStackFromNBT(tagCompound), true);
                        }
                    }
                    player.addChatComponentMessage(new TextComponentString("Rarmor has updated. All of you Items are dropped to the ground, to avoid the loss of them."));
                }
            }
        }
    }

    public static void syncTab(EntityPlayerMP player, IRarmorTab tab){
        ItemStack stack = getRarmorChestplate(player);
        int id = RarmorAPI.registeredTabs.indexOf(tab.getClass());
        IMessage p = new PacketRarmorPacketData(tab.getPacketData(player, stack), id);
        PacketHandler.network.sendTo(p, player);
    }

    @SideOnly(Side.CLIENT)
    public static void syncBoolToServer(EntityPlayer player, IRarmorTab tab, int key, boolean bool){
        PacketHandler.network.sendToServer(new PacketSendBoolean(tab, key, bool));
    }

    public static void requestEnergy(BlockPos pos, EnumFacing side){
        PacketHandler.network.sendToServer(new PacketRequestEnergyLevel(pos, side));
    }

}
