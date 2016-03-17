package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.api.GuiCheckBox;
import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.rarmor.network.NetworkHandler;
import de.canitzp.rarmor.network.PacketSendNBTBoolean;
import de.canitzp.rarmor.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class ItemModuleMovement extends ItemModule implements IRarmorModule{

    private int cb1X = 117, cb1Y = 12;
    private List<GuiCheckBox> boxes = new ArrayList<>();

    public ItemModuleMovement() {
        super("moduleMovement");
    }

    @Override
    public String getUniqueName() {
        return "Movement";
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory) {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initGui(World world, EntityPlayer player, ItemStack armorChestplate, GuiContainer gui, List<GuiCheckBox> checkBoxes, ResourceLocation checkBoxResource){
        IInventory inventory = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount);
        GuiCheckBox cb1 = new GuiCheckBox(gui, checkBoxResource, cb1X, cb1Y, 10, 10, "2 Block Walk", null);
        boxes.add(cb1);
        cb1.setClicked(NBTUtil.getBoolean(inventory.getStackInSlot(ItemRFArmorBody.MODULESLOT), "2BlockWalk"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY, int guiLeft, int guiTop) {
        if(!settingActivated){
            for(GuiCheckBox checkBox : boxes){
                checkBox.drawCheckBox(guiLeft, guiTop);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY, int guiLeft, int guiTop) {
        if(!settingActivated){
            for(GuiCheckBox checkBox : boxes){
                if(checkBox.mouseClicked(mouseX, mouseY, guiLeft, guiTop)){
                    IInventory inventory = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount);
                    NetworkHandler.wrapper.sendToServer(new PacketSendNBTBoolean(minecraft.thePlayer, "2BlockWalk", checkBox.isClicked()));
                    NBTUtil.setBoolean(module, "2BlockWalk", checkBox.isClicked());
                }
            }
        }
    }

}
