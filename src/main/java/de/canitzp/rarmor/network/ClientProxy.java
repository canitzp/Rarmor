package de.canitzp.rarmor.network;

import de.canitzp.rarmor.RarmorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author canitzp
 */
public class ClientProxy extends CommonProxy{

    @Override
    public void preInit(FMLPreInitializationEvent event){
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void openGui(GuiOpenEvent event){
        if(event.getGui() instanceof GuiInventory){
            if(RarmorUtil.isPlayerWearingArmor(Minecraft.getMinecraft().thePlayer)){
                event.setCanceled(true);
                network.sendToServer(new PacketOpenGui(Minecraft.getMinecraft().thePlayer, 0));
            }
        }
    }
}
