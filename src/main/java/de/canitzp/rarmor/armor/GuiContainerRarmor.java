package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import de.canitzp.rarmor.network.PacketSetTab;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public class GuiContainerRarmor extends Container{

    private IRarmorTab activeTab;
    private EntityPlayer player;
    private InventoryBasic energyField = new InventoryBasic("Energy Field", false, 2);

    public GuiContainerRarmor(EntityPlayer player) {
        RarmorUtil.tryToRemap(player);
        this.player = player;
        this.readFromNBT(NBTUtil.getTagFromStack(RarmorUtil.getRarmorChestplate(player)));
        this.activeTab.initContainer(this, player);

        List<Slot> slots = new ArrayList<>();
        //Player Hotbar: 0-8
        for (int j = 0; j < 9; ++j) {
            slots.add(new Slot(player.inventory, j, 44 + j * 18, 202));
        }
        //Player Inventory: 9-35
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                slots.add(new Slot(player.inventory, k + j * 9 + 9, 44 + k * 18, 144 + j * 18));
            }
        }
        slots.addAll(this.activeTab.manipulateSlots(this, this.player, new ArrayList<>(), this.energyField, 4, 4));
        for (Slot slot : slots) {
            this.addSlotToContainer(slot);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn){
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        this.writeToNBT(NBTUtil.getTagFromStack(RarmorUtil.getRarmorChestplate(player)));
        super.onContainerClosed(player);
    }

    @Override
    public void addListener(IContainerListener listener){
        super.addListener(listener);
        this.activeTab.addListener(this, this.player, listener);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        nbt.setInteger("RarmorTabID", RarmorAPI.registeredTabs.indexOf(this.activeTab.getClass()));
        if(!this.player.worldObj.isRemote){
            NBTUtil.writeInventory(nbt, this.energyField);
            for(IRarmorTab tab : RarmorAPI.getTabsFromStack(this.player.getEntityWorld(), RarmorUtil.getRarmorChestplate(player))){
                ItemStack stack = RarmorUtil.getRarmorChestplate(this.player);
                NBTTagCompound tabNBT = nbt.getCompoundTag(tab.getTabIdentifier(stack, this.player));
                nbt.setTag(tab.getTabIdentifier(stack, this.player), tab.writeToNBT(tabNBT));
            }
        }
        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt){
        this.activeTab = RarmorAPI.getTabsFromStack(this.player.worldObj, RarmorUtil.getRarmorChestplate(this.player)).get(nbt.getInteger("RarmorTabID"));
        if(!player.worldObj.isRemote){
            this.energyField = NBTUtil.readInventory(nbt, this.energyField);
        }
    }

    public void setTabPacket(int tabID){
        ItemStack stack = RarmorUtil.getRarmorChestplate(this.player);
        NBTTagCompound nbt = this.writeToNBT(NBTUtil.getTagFromStack(stack));
        nbt.setInteger("RarmorTabID", tabID);
        NBTUtil.setTagFromStack(stack, nbt);
        this.player.openGui(Rarmor.instance, 0, this.player.worldObj, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static class GuiRarmor extends GuiContainer{
        final ResourceLocation guiLoc = new ResourceLocation(Rarmor.MODID, "textures/gui/guiRarmor.png");
        private IRarmorTab tab;
        private List<RarmorTab> tabs = new ArrayList<>();
        public EntityPlayer player;
        private List<IRarmorTab> registeredTabs;
        public GuiRarmor(EntityPlayer player){
            super(new GuiContainerRarmor(player));
            this.player = player;
            ItemStack stack = RarmorUtil.getRarmorChestplate(player);
            registeredTabs = RarmorAPI.getTabsFromStack(player.getEntityWorld(), stack);
            this.tab = registeredTabs.get(NBTUtil.getTagFromStack(stack).getInteger("RarmorTabID"));
            sortTabs();
            this.xSize = 247;
            this.ySize = 226;
        }
        @Override
        public void initGui(){
            super.initGui();
            tab.initGui(this, this.player);
        }
        public void sortTabs(){
            int j = 0;
            for(int i = 0; i < registeredTabs.size(); i++){
                IRarmorTab tab = registeredTabs.get(i);
                if(tab.canBeVisible(RarmorUtil.getRarmorChestplate(player), player)){
                    tabs.add(new RarmorTab(j, tab, tab.getClass().equals(this.tab.getClass())));
                    j++;
                }
            }
        }
        @Override
        public void drawHoveringText(List<String> textLines, int x, int y){
            super.drawHoveringText(textLines, x, y);
        }
        @Override
        protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(guiLoc);
            this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
            for(RarmorTab tab : tabs){
                tab.draw(this, this.guiLeft, this.guiTop);
                this.mc.getTextureManager().bindTexture(guiLoc);
            }
            this.tab.drawGui(this, this.player, this.guiLeft, this.guiTop, mouseX, mouseY, partialTicks);
        }
        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks){
            super.drawScreen(mouseX, mouseY, partialTicks);
            for(RarmorTab tab : tabs){
                tab.drawText(this, this.guiLeft, this.guiTop, mouseX, mouseY);
            }
            this.tab.drawForeground(this, this.player, this.guiLeft, this.guiTop, mouseX, mouseY, partialTicks);
        }
        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
            if(mouseButton == 0){
                for(RarmorTab tab : this.tabs){
                    tab.onMouseClick(this, this.guiLeft, this.guiTop, mouseX, mouseY);
                }
            }
            if(this.guiLeft + 4 < mouseX && this.guiLeft + 239 > mouseX && this.guiTop + 4 < mouseY && this.guiTop + 138 > mouseY){
                this.tab.onMouseClick(this, this.player, this.guiLeft, this.guiTop, mouseX, mouseY, mouseButton);
            }
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        public void setTab(IRarmorTab tab){
            if(tab != null){
                NBTUtil.getTagFromStack(RarmorUtil.getRarmorChestplate(this.player)).setInteger("RarmorTabID", RarmorAPI.registeredTabs.indexOf(tab.getClass()));
                Rarmor.proxy.network.sendToServer(new PacketSetTab(this.player, tab));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static class RarmorTab extends Gui{
        private int width = 24, height = 21;
        private String hoveringText;
        private boolean active;
        private int i;
        private IRarmorTab tab;
        public RarmorTab(int i, IRarmorTab tab, boolean active){
            this.hoveringText = tab.getTabHoveringText();
            this.tab = tab;
            this.active = active;
            this.i = i;
        }
        public void draw(GuiContainer gui, int guiLeft, int guiTop){
            gui.drawTexturedModalRect((this.i * this.width + 3) + guiLeft, guiTop - this.height, this.active ? 0 : this.width, 226, this.width, this.height + (this.active ? 3 : 0));
            if(this.tab.getTabIcon() != null){
                RarmorUtil.renderStackToGui(this.tab.getTabIcon(), (this.i * width + 3) + 4 + guiLeft, guiTop - this.height + 4, 1.0F);
            } else {
                this.tab.drawTab(gui, gui.mc.thePlayer, (this.i * width + 3) + 4 + guiLeft, guiTop - this.height + 4);
            }
        }
        public void drawText(GuiRarmor gui, int guiLeft, int guiTop, int mouseX, int mouseY){
            if(isMouseOver(guiLeft, guiTop, mouseX, mouseY) && this.hoveringText != null){
                gui.drawHoveringText(Collections.singletonList(this.hoveringText), mouseX, mouseY);
            }
        }
        public void onMouseClick(GuiRarmor gui, int guiLeft, int guiTop, int mouseX, int mouseY){
            if(isMouseOver(guiLeft, guiTop, mouseX, mouseY)){
                gui.setTab(this.tab);
            }
        }
        public boolean isMouseOver(int guiLeft, int guiTop, int mouseX, int mouseY){
            return ((this.i + 1) * this.width + 2) + guiLeft >= mouseX && guiTop >= mouseY && (this.i * this.width + 2) + guiLeft <= mouseX && guiTop - this.height <= mouseY;
        }
    }
}
