package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.NBTUtil;
import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorAPI;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public class GuiContainerRarmor extends Container{

    private IRarmorTab activeTab;

    public GuiContainerRarmor(EntityPlayer player){
        this.activeTab = RarmorAPI.getTab(NBTUtil.getInteger(RarmorUtil.getRarmorChestplate(player), "RarmorTabID"));
        this.activeTab.initContainer(this, player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn){
        return true;
    }

    @SideOnly(Side.CLIENT)
    public static class GuiRarmor extends GuiContainer{
        final ResourceLocation guiLoc = new ResourceLocation(Rarmor.MODID, "textures/gui/guiRarmor.png");
        private IRarmorTab tab;
        private List<RarmorTab> tabs = new ArrayList<>();
        public EntityPlayer player;
        public GuiRarmor(EntityPlayer player){
            super(new GuiContainerRarmor(player));
            this.player = player;
            this.tab = RarmorAPI.getTab(NBTUtil.getInteger(RarmorUtil.getRarmorChestplate(player), "RarmorTabID"));
            List<IRarmorTab> registeredTabs = RarmorAPI.registeredTabs;
            for(int i = 0; i < registeredTabs.size(); i++){
                IRarmorTab tab = registeredTabs.get(i);
                tabs.add(new RarmorTab(i, tab, tab.equals(this.tab)));
            }
            this.xSize = 247;
            this.ySize = 226;
        }
        @Override
        public void initGui(){
            super.initGui();
            tab.initGui(this, this.player);
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
                tab.draw(this.guiLeft, this.guiTop);
            }
            this.tab.drawGui(this, this.guiLeft, this.guiTop, mouseX, mouseY, partialTicks);
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks){
            super.drawScreen(mouseX, mouseY, partialTicks);
            for(RarmorTab tab : tabs){
                tab.drawText(this, this.guiLeft, this.guiTop, mouseX, mouseY);
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
        public void draw(int guiLeft, int guiTop){
            this.drawTexturedModalRect(guiLeft + 2, guiTop - this.height, (this.i * this.width - 1) + (this.active ? this.width+1 : 0), 226, this.width, this.height);
            if(this.tab.getTabIcon() != null){
                RarmorUtil.renderStackToGui(this.tab.getTabIcon(), (this.i * width + 2) + 4 + guiLeft, guiTop - this.height + 3, 1.0F);
            } else {
                this.tab.drawTab();
            }
        }
        public void drawText(GuiRarmor gui, int guiLeft, int guiTop, int mouseX, int mouseY){
            if(isMouseOver(guiLeft, guiTop, mouseX, mouseY) && this.hoveringText != null){
                gui.drawHoveringText(Collections.singletonList(this.hoveringText), mouseX, mouseY);
            }
        }
        public boolean isMouseOver(int guiLeft, int guiTop, int mouseX, int mouseY){
            return ((this.i + 1) * this.width + 2) + guiLeft >= mouseX && guiTop >= mouseY && (this.i * this.width + 2) + guiLeft <= mouseX && guiTop - this.height <= mouseY;
        }
    }
}
