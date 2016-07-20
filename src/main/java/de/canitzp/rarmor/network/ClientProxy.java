package de.canitzp.rarmor.network;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.Registry;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.api.RarmorSettings;
import de.canitzp.rarmor.api.RarmorValues;
import de.canitzp.rarmor.api.tooltip.IInWorldTooltip;
import de.canitzp.rarmor.armor.ItemGenericRarmor;
import de.canitzp.rarmor.armor.ItemRarmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy{

    @Override
    public void preInit(FMLPreInitializationEvent event){
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void init(FMLInitializationEvent event){
        super.init(event);
        registerRenderer();
        registerColoring();
    }

    private void registerRenderer(){
        for(Map.Entry<ItemStack, String> entry : this.textures.entrySet()){
            if(entry.getKey() != null){
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(entry.getKey().getItem(), 0, new ModelResourceLocation(RarmorValues.MODID + ":" + entry.getValue(), "inventory"));
            }
        }
    }

    public void registerColoring(){
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                return NBTUtil.getColor(stack);
            }
        }, Registry.rarmorChestplate, Registry.rarmorBoots, Registry.rarmorLeggins, Registry.rarmorHelmet);
    }

    @SubscribeEvent
    public void openGui(GuiOpenEvent event){
        if(event.getGui() instanceof GuiInventory){
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if(RarmorUtil.isPlayerWearingArmor(player)){
                List<IRarmorTab> tabs = RarmorAPI.getTabsFromStack(Minecraft.getMinecraft().theWorld, RarmorUtil.getRarmorChestplate(player));
                if(tabs != null && !tabs.isEmpty()){
                    for(IRarmorTab tab : tabs){
                        tab.preOpen(player, RarmorUtil.getRarmorChestplate(player));
                    }
                }
                if(player.isSneaking() == RarmorSettings.getSettingBoolean(RarmorUtil.getRarmorChestplate(player), RarmorSettings.Settings.INVERTED_OPENING)){
                    event.setCanceled(true);
                    PacketHandler.network.sendToServer(new PacketOpenGui(0));
                }
            }
        }
    }

    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent.Post event){
        if(event.getType().equals(RenderGameOverlayEvent.ElementType.ALL) && Minecraft.getMinecraft().currentScreen == null){
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            WorldClient world = Minecraft.getMinecraft().theWorld;
            ItemStack stack = RarmorUtil.getRarmorChestplate(player);
            ItemStack stack1 = RarmorUtil.getPlayerArmorPart(player, EntityEquipmentSlot.HEAD);
            boolean isHelmet = stack1 != null && stack1.getItem() instanceof ItemGenericRarmor;
            if(stack != null && stack.getItem() instanceof ItemRarmor){
                ((ItemRarmor) stack.getItem()).renderInWorld(world, player, stack, event.getResolution(), Minecraft.getMinecraft().fontRendererObj, event.getType(), event.getPartialTicks(), isHelmet);
            }
            for(IInWorldTooltip tooltip : RarmorAPI.getInWorldTooltips()){
                tooltip.showTooltip(world, player, stack, event.getResolution(), Minecraft.getMinecraft().fontRendererObj, event.getType(), event.getPartialTicks(), isHelmet);
            }
        }
    }

}
