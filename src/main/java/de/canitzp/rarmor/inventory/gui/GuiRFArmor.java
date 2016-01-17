package de.canitzp.rarmor.inventory.gui;

import com.google.common.collect.Lists;
import de.canitzp.api.util.NBTUtil;
import de.canitzp.api.util.PacketUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.inventory.container.ContainerRFArmor;
import de.canitzp.rarmor.inventory.container.Slots.SlotModule;
import de.canitzp.rarmor.items.rfarmor.ItemModuleGenerator;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmor;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author canitzp
 */
public class GuiRFArmor extends GuiContainer {

    private EntityPlayer player;
    private ItemStack armor;
    private ResourceLocation normalGui = new ResourceLocation(Rarmor.MODID, "textures/rfarmor/gui/guiRFArmorNormal.png");
    private ResourceLocation modulesGui = new ResourceLocation(Rarmor.MODID, "textures/rfarmor/gui/guiRFArmorModules.png");
    private float xSizeFloat;
    private float ySizeFloat;

    public GuiRFArmor(EntityPlayer player, ContainerRFArmor containerRFArmor) {
        super(containerRFArmor);
        this.xSize = 247;
        this.ySize = 226;
        this.player = player;
        this.armor = player.getCurrentArmor(ItemRFArmor.ArmorType.BODY.getId() + 1);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(normalGui);
        //Draw Gui:
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        ItemRFArmorBody body = (ItemRFArmorBody) armor.getItem();
        int energy = body.getEnergyStored(armor);
        int factorOfEnergy = energy * 21 / body.maxEnergy;
        int factorOfBurnTime = (int) (NBTUtil.getInteger(armor, "BurnTime") * 11.9 / 200);
        int i = 0;
        if(NBTUtil.getInteger(armor, "CurrentItemGenBurnTime") != 0){
            i = (NBTUtil.getInteger(armor, "CurrentItemGenBurnTime") - NBTUtil.getInteger(armor, "GenBurnTime")) * 13 / NBTUtil.getInteger(armor, "CurrentItemGenBurnTime");
        }

        //Draw Batterie:
        this.drawTexturedModalRect(this.guiLeft + 18, this.guiTop + 29 - factorOfEnergy, 55, 247 - factorOfEnergy, 10, factorOfEnergy);
        //Draw Furnace Burn Time:
        this.drawTexturedModalRect(this.guiLeft + 15, this.guiTop + 89 - factorOfBurnTime, 39, 237 - factorOfBurnTime, 16, factorOfBurnTime);

        if(NBTUtil.getBoolean(armor, "ModuleGenerator")){
            this.mc.getTextureManager().bindTexture(modulesGui);
            this.drawTexturedModalRect(this.guiLeft + 120, this.guiTop + 13, 57, 0, 56, 55);
            this.drawTexturedModalRect(this.guiLeft + 141, this.guiTop + 39 - i + 13, 58, 57 - i + 13, 13, i);
        }

        GuiInventory.func_147046_a(this.guiLeft + 88, this.guiTop + 77, 34, (float)(this.guiLeft + 88) - this.xSizeFloat, (float)(this.guiTop + 72 - 50) - this.ySizeFloat, this.mc.thePlayer);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        this.xSizeFloat = (float)mouseX;
        this.ySizeFloat = (float)mouseY;
        super.drawScreen(mouseX, mouseY, par3);

        if(mouseX >= this.guiLeft + 18 && mouseY >= this.guiTop + 8 && mouseX <= this.guiLeft + 27 &&mouseY <= this.guiTop + 28){
            int energy = ((ItemRFArmorBody) armor.getItem()).getEnergyStored(armor);
            int cap = ((ItemRFArmorBody) armor.getItem()).getMaxEnergyStored(armor);
            this.drawHoveringText(Lists.newArrayList(Integer.toString(energy) + "/" + Integer.toString(cap) + " RF"), mouseX, mouseY, this.fontRendererObj);
        }
        if(mouseX >= this.guiLeft + 8 && mouseY >= this.guiTop + 136 && mouseX <= this.guiLeft + 29 &&mouseY <= this.guiTop + 156) {
            this.drawHoveringText(Lists.newArrayList("Back to normal Inventory"), mouseX, mouseY, this.fontRendererObj);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int type){
        if(type == 0){
            if(mouseX >= this.guiLeft + 8 && mouseY >= this.guiTop + 136 && mouseX <= this.guiLeft + 29 &&mouseY <= this.guiTop + 156) {
                this.armor.stackTagCompound.setBoolean("click", true);
                PacketUtil.openPlayerInventoryFromClient(this.mc, this);
                //this.onGuiClosed();
                //EntityPlayer player = this.mc.thePlayer;
                //mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(player.openContainer.windowId));
                //GuiInventory inventory = new GuiInventory(player);
                //mc.displayGuiScreen(inventory);
            }
        }
        super.mouseClicked(mouseX, mouseY, type);
    }

    @Override
    protected boolean func_146978_c(int slotX, int slotY, int width, int height, int mouseX, int mouseY) {
        int k1 = this.guiLeft;
        int l1 = this.guiTop;
        mouseX -= k1;
        mouseY -= l1;
        boolean b = mouseX >= slotX - 1 && mouseX < slotX + width + 1 && mouseY >= slotY - 1 && mouseY < slotY + height + 1;
        Slot slot = this.getSlotAtPosition(slotX, slotY);
        Slot moduleSlot = this.getSlotAtPosition(15, 34);
        if(moduleSlot != null){
            if(moduleSlot.getStack() != null){
                if(moduleSlot.getStack().getItem() instanceof ItemModuleGenerator){
                    return b;
                }
            }
            return b && !(slot instanceof SlotModule);
        }
        return b;
    }

    private Slot getSlotAtPosition(int x, int y) {
        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k) {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(k);
            if(x >= slot.xDisplayPosition - 1 && x < slot.xDisplayPosition + 16 + 1 && slot.yDisplayPosition >= y - 1 && slot.yDisplayPosition < y + 16 + 1){
                return slot;
            }
        }
        return null;
    }

}
