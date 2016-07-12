package de.canitzp.rarmor.network;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

/**
 * @author canitzp
 */
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
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(entry.getKey().getItem(), 0, new ModelResourceLocation(Rarmor.MODID + ":" + entry.getValue(), "inventory"));
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
            if(!Minecraft.getMinecraft().thePlayer.isSneaking() && RarmorUtil.isPlayerWearingArmor(Minecraft.getMinecraft().thePlayer)){
                event.setCanceled(true);
                network.sendToServer(new PacketOpenGui(Minecraft.getMinecraft().thePlayer, 0));
            }
        }
    }
}
