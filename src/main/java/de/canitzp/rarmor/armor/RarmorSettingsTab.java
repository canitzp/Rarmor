package de.canitzp.rarmor.armor;

import de.canitzp.rarmor.Rarmor;
import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.IRarmorTab;
import de.canitzp.rarmor.api.RarmorSettings;
import de.canitzp.rarmor.api.gui.GuiCheckBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

/**
 * @author canitzp
 */
public class RarmorSettingsTab implements IRarmorTab {

    private Map<GuiCheckBox, RarmorSettings.Settings> checks = new HashMap<>();

    public RarmorSettingsTab(){
        this.checks.put(new GuiCheckBox(15, 15, "Invert Rarmor/Inventory Opening", Collections.singletonList("")), RarmorSettings.Settings.INVERTED_OPENING);
    }

    @Override
    public String getTabIdentifier(ItemStack rarmor, EntityPlayer player) {
        return Rarmor.MODID + ":settingsTab";
    }

    @Override
    public void initContainer(Container container, EntityPlayer player) {
        if(!player.worldObj.isRemote){
            RarmorUtil.syncTab((EntityPlayerMP) player, this);
        }
    }

    @Override
    public void drawGui(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        for(GuiCheckBox box : this.checks.keySet()){
            box.draw(gui, guiLeft, guiTop);
        }
    }

    @Override
    public void drawForeground(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        for(GuiCheckBox box : this.checks.keySet()){
            box.drawForeground(gui, guiLeft, guiTop, mouseX, mouseY);
        }
    }

    @Override
    public void onMouseClick(GuiContainer gui, EntityPlayer player, int guiLeft, int guiTop, int mouseX, int mouseY, int btnID) {
        if(btnID == 0){
            for(GuiCheckBox box : this.checks.keySet()){
                if(box.isMouseOver(guiLeft, guiTop, mouseX, mouseY)){
                    box.onMouseClick(guiLeft, guiTop, mouseX, mouseY);
                    RarmorSettings.setSetting(RarmorUtil.getRarmorChestplate(player), this.checks.get(box), box.getState());
                    RarmorUtil.syncBoolToServer(player, this, this.checks.get(box).ordinal(), box.getState());
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        for(GuiCheckBox box : this.checks.keySet()){
            box.writeToNBT(nbt);
        }
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        for(GuiCheckBox box : this.checks.keySet()){
            box.readFromNBT(nbt);
        }
    }

    @Override
    public NBTTagCompound getPacketData(EntityPlayerMP player, ItemStack stack) {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void onPacketData(EntityPlayer player, ItemStack stack, NBTTagCompound nbt) {
        this.readFromNBT(nbt);
        for(Map.Entry<GuiCheckBox, RarmorSettings.Settings> entry : this.checks.entrySet()){
            RarmorSettings.setSetting(stack, entry.getValue(), entry.getKey().getState());
        }
    }

    @Override
    public void onPacketBool(EntityPlayer player, int id, boolean bool) {
        for(Map.Entry<GuiCheckBox, RarmorSettings.Settings> entry : this.checks.entrySet()){
            if(entry.getValue().ordinal() == id){
                entry.getKey().setState(bool);
                RarmorSettings.setSetting(RarmorUtil.getRarmorChestplate(player), entry.getValue(), bool);
            }
        }
    }

}
