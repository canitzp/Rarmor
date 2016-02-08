package de.canitzp.rarmor.inventory.gui;

import com.google.common.collect.Maps;
import de.canitzp.rarmor.api.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author canitzp
 */
public class GuiCircuitCreator extends GuiContainer {

    private EntityPlayer player;
    private World world;
    private ResourceLocation mainGui = RamorResources.CIRCUITCREATORGUI.getNewLocation();
    private List<IElectricalComponent> electricalComponents = new ArrayList<>();
    private HashMap<IElectricalComponent, IElectricalItem> componentItem = Maps.newHashMap();
    public GuiSetting setting;
    //TODO!!!
    private NBTTagCompound compound = new NBTTagCompound();

    public GuiCircuitCreator(EntityPlayer player, Container inventorySlotsIn) {
        super(inventorySlotsIn);
        this.player = player;
        this.world = player.getEntityWorld();
        this.xSize = this.ySize = 256;
    }

    @Override
    public void initGui() {
        super.initGui();
        setting = new GuiSetting();
        for (IElectricalComponent component : electricalComponents) {
            if (component != null) {
                if (component instanceof IGuiInteraction) {
                    ((IGuiInteraction) component).initGui(this, this.setting, this.world, this.player);
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(!electricalComponents.isEmpty()){
            for (IElectricalComponent component : electricalComponents) {
                if (component != null) {
                    if (component instanceof IGuiInteraction) {
                        if (!((IGuiInteraction) component).onKeyPressed(this, this.setting, this.world, this.player, typedChar, keyCode)) {
                            super.keyTyped(typedChar, keyCode);
                        }
                    }
                }
            }
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(mainGui);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        for (IElectricalComponent component : electricalComponents) {
            if (component != null) {
                this.mc.getTextureManager().bindTexture(component.guiComponent());
                this.drawTexturedModalRect(component.getX() + guiLeft, component.getY() + guiTop, component.getTextureX(), component.getTextureY(), component.getTextureWidth(), component.getTextureHeight());
                if (component instanceof IGuiInteraction) {
                    if(this.setting.areComponentsEqual(component)){
                        ((IGuiInteraction) component).drawGuiContainerBackgroundLayer(this, this.setting, this.world, this.player, this.guiLeft, this.guiTop, partialTicks, mouseX, mouseY);
                        this.mc.getTextureManager().bindTexture(mainGui);
                    }
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (IElectricalComponent component : electricalComponents) {
            if (component != null) {
                //this.drawHoveringText(component.getHoveringText(), component.getX() + guiLeft, component.getY() + guiTop);
                component.render(this, this.fontRendererObj);
                if (component instanceof IGuiInteraction) {
                    ((IGuiInteraction) component).drawScreen(this, this.setting, this.world, this.player, mouseX, mouseY, partialTicks);
                    ((IGuiInteraction) component).mouseHover(this, this.setting, this.world, this.player, this.guiLeft, this.guiTop, mouseX, mouseY);
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (IElectricalComponent component : electricalComponents) {
            if (component != null) {
                if (component.getX() + guiLeft <= mouseX && component.getX() + guiLeft + component.getWidth() >= mouseX) {
                    if (component.getY() + guiTop <= mouseY && component.getY() + guiTop + component.getHeight() >= mouseY) {
                        if (component.getX() + guiLeft <= mouseX && component.getX() + guiLeft + component.getTextureWidth() >= mouseX) {
                            if (component.getY() + guiTop <= mouseY && component.getY() + guiTop + component.getTextureHeight() >= mouseY) {
                                if(!setting.renderer.isEmpty()){
                                    component.onGuiSettingsClosed(setting);
                                    return;
                                }
                            }
                        }
                        if (component instanceof IGuiInteraction) {
                            ((IGuiInteraction) component).mouseClicked(this, this.setting, this.world, this.player, this.guiLeft, this.guiTop, mouseX, mouseY, mouseButton);
                            return;
                        }
                    }
                }
            }
        }
        if(!setting.renderer.isEmpty()){
            setting.reinit();
            super.mouseClicked(mouseX, mouseY, mouseButton);
            return;
        }
        if (mouseX >= guiLeft + 7 && mouseX <= guiLeft + 249 && mouseY >= guiTop + 7 && mouseY <= guiTop + 170) {
            ItemStack draggedStack = player.inventory.getItemStack();
            if(draggedStack != null){
                if(draggedStack.getItem() instanceof IElectricalItem){
                    IElectricalComponent base = ((IElectricalItem) draggedStack.getItem()).getComponent(guiLeft, guiTop, mouseX, mouseY);
                    this.electricalComponents.add(base);
                    this.componentItem.put(base, (IElectricalItem) draggedStack.getItem());
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void addElectricalComponent(IElectricalComponent component) {
        this.electricalComponents.add(component);
    }

    public void removeElectricalComponent(IElectricalComponent component) {
        if (this.electricalComponents.contains(component)) {
            this.electricalComponents.remove(component);
        }
    }


}
