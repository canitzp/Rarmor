package de.canitzp.rarmor.items;

import de.canitzp.rarmor.api.IRarmorModule;
import de.canitzp.util.util.EnergyUtil;
import de.canitzp.util.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * @author canitzp
 */
public class ItemModuleSolarPanel extends ItemModule implements IRarmorModule {

    public ItemModuleSolarPanel() {
        super("moduleSolarPanel");
    }

    @Override
    public String getUniqueName() {
        return "SolarPanel";
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory) {
        if(NBTUtil.getInteger(module, "tick") >= 50){
            if(canPlayerSeeSky(player)){
                EnergyUtil.addEnergy(armorChestplate, 2, armorChestplate.getMaxDamage());
                NBTUtil.setInteger(module, "tick", 0);
                NBTUtil.setBoolean(module, "doWork", true);
            } else {
                NBTUtil.setBoolean(module, "doWork", false);
            }
        } else {
            NBTUtil.setInteger(module, "tick", NBTUtil.getInteger(module, "tick") + 1);
            if(NBTUtil.getBoolean(module, "doWork")){
                EnergyUtil.addEnergy(armorChestplate, 2, armorChestplate.getMaxDamage());
            }
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, IInventory inventory) {}

    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY) {}

    @Override
    public void drawScreen(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY) {}

    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY) {}

    @Override
    public boolean showSlot(Minecraft minecraft, GuiContainer gui, ItemStack module, boolean settingActivated, Slot slot, int mouseX, int mouseY, int slotX, int slotY, boolean isMouseOverSlot) {
        return isMouseOverSlot;
    }

    private boolean canPlayerSeeSky(EntityPlayer player) {
        if(!player.worldObj.isRaining()){
            for(int i = (int) player.posY; i <= 256; i++){
                if(!player.worldObj.isAirBlock(new BlockPos(player.posX, i, player.posZ))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
